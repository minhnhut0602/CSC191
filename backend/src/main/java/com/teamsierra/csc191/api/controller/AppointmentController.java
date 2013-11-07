package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.AppointmentType;
import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.AppointmentTypeRepository;
import com.teamsierra.csc191.api.resources.ResourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author Alex Chernyak
 * Controller managing appointment business logic
 *
 * Public methods:
 * getAppointments(HttpServletRequest)
 * gets all appointments relevant to the user type of a caller
 *
 * getAppointment(String, HttpServletRequest)
 * get a specific requestData specificed a appointmentID
 *
 * searchForAppointments(Appointment, HttpServletRequest)
 * search appointments that match search fields
 *
 * addAppointment(Appointment, HttpServletRequest)
 * add requestData to database
 *
 * editAppointment(Appointment, String, HttpServletRequest)
 * edit an exisit requestData
 *
 */
@Controller
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentController extends GenericController
{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    /**
     * Get list of all appointments relevant to a caller
     * @return list of appointments
     * @throws Exception
     * TODO date filter??
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Resource<Appointment>>> getAppointments(HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);

        Appointment findAppointment = new Appointment();
        List<Resource<Appointment>> appointmentResources = new ArrayList<>();

        // Set search constraints based on authType
        switch (this.authType)
        {
            case CLIENT:
                findAppointment.setClientID(this.id);
            break;

            case STYLIST:
                findAppointment.setStylistID(this.id);
            break;

            case ADMIN:
            break;

            default:
            break;
        }

        for(Appointment appointment: appointmentRepository.findByCriteria(findAppointment))
            appointmentResources.add(ResourceHandler.createResource(appointment));

        if (appointmentResources.isEmpty())
            throw new Exception("Search returned empty set");

        return new ResponseEntity<>(appointmentResources, HttpStatus.OK);
    }


    /**
     * Get a specific appointment
     * @param appointmentID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{appointmentID}",  method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource<Appointment>> getAppointment(@PathVariable String appointmentID,
                                                                HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);

        Appointment targetAppointment = new Appointment();
        Resource<Appointment> appointmentResource;
        List<Appointment> appointments;

        // Set search constraints based on authType
        targetAppointment.setId(appointmentID);
        switch (this.authType)
        {
            case CLIENT:
                targetAppointment.setClientID(id);
            break;

            case STYLIST:
                targetAppointment.setStylistID(id);
            break;

            case ADMIN:
            break;

            default:
            break;
        }

        appointments = appointmentRepository.findByCriteria(targetAppointment);

        if (appointments == null || appointments.isEmpty() || appointments.size() == 0)
            throw new Exception("Appointment with supplied id cannot be found");

        appointmentResource = ResourceHandler.createResource(appointments.get(0));

        return new ResponseEntity<>(appointmentResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Resource<Appointment>>> searchForAppointments(@RequestBody Appointment searchFilters,
                                                                             HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);

        List<Resource<Appointment>> appointmentResources = new ArrayList<>();
        List<Appointment> appointments;

        // Set search constraints based on authType
        switch (this.authType)
        {
            case CLIENT:
                searchFilters.setClientID(id);
            break;

            case STYLIST:
                searchFilters.setStylistID(id);
            break;

            case ADMIN:
            break;

            default:
            break;
        }

        appointments = appointmentRepository.findByCriteria(searchFilters);
        if (appointments.isEmpty())
            throw new Exception("Search returned empty set");

        for(Appointment appointment: appointments)
            appointmentResources.add(ResourceHandler.createResource(appointment));

        return new ResponseEntity<>(appointmentResources, HttpStatus.OK);
    }


    /**
     * Add a single appointment
     * @param requestData
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Resource<GenericModel>> addAppointment(@RequestBody Appointment requestData,
                                                                 HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        User client, stylist;
        Appointment targetAppointment = new Appointment();
        List<Appointment> appointments;
        AppointmentType apType = new AppointmentType();

        StringBuilder errors = new StringBuilder("");
        switch (this.authType)
        {
            case CLIENT:
                requestData.setClientID(id);
                requestData.setAppointmentStatus(GenericModel.AppointmentStatus.NEW);
                break;

            case STYLIST:
                requestData.setStylistID(id);
                break;

            case ADMIN:
            break;

            default:
            break;
        }

        // Check for missing request parameters
        if (requestData.getClientID().isEmpty())
            errors.append("clientID ");
        if (requestData.getStylistID().isEmpty())
            errors.append("stylistID ");
        if (requestData.getStartTime() == null)
            errors.append("startTime ");
        if (requestData.getEndTime() == null)
            errors.append("endTime ");
        if (errors.length() > 0)
            throw new Exception("Request is missing field(s): " + errors.toString());

        // Validate supplied appointment date/time parameters
        Date currentTimestamp = Calendar.getInstance().getTime();
        if (requestData.getStartTime().before(requestData.getEndTime()))
        {
            Date temp = requestData.getStartTime();
            requestData.setStartTime(requestData.getEndTime());
            requestData.setEndTime(temp);
        }

        // Check if dates are valid
        if (requestData.getStartTime().before(currentTimestamp) ||
            requestData.getEndTime().before(currentTimestamp))
            throw new Exception("Appointments cannot be created in the past");

        //TODO user and stylist validation needs to be checked on id AND userType

        // Validate stylist
        stylist = userRepository.findById(requestData.getStylistID());
        if (stylist == null)
            throw new Exception("Invalid stylistID supplied");
        else
        {
            /*
             * TODO add hours of availability to stylists (User model) and validate requested dates
             * this does not require calling appointment repository.
             * if dates supplied outside of stylist range, throw an exception
             */
        }

        // Validate client
        client = userRepository.findById(requestData.getClientID());
        if (client == null)
            throw new Exception("Invalid clientID supplied");
        else
        {
            // TODO validate user data
            /*
            if (!client.isActive())
                throw new Exception("Cannot schedule an requestData for de-activated clients");
            */
        }

        // Validate requested appointment dates
        // These 4 properties define an appointment, other properties are for description and shouldn't be used
        targetAppointment.setStartTime(requestData.getStartTime());
        targetAppointment.setEndTime(requestData.getEndTime());
        targetAppointment.setStylistID(requestData.getStylistID());
        targetAppointment.setClientID(requestData.getClientID());
        appointments = appointmentRepository.findByCriteria(targetAppointment);
        for (Appointment a: appointments)
        {
            switch (a.getAppointmentStatus())
            {
                case APPROVED:
                case NEW:
                    throw new Exception("Given date/time range conflicts with existing appointments");
                default:
            }
        }

        // Validate appointment type
        if (requestData.getAppointmentTypeID() == null || requestData.getAppointmentTypeID().isEmpty())
            throw new Exception("A valid appointment type id is required");
        else
        {
            String[] stylists = { requestData.getStylistID() };

            apType.setStylists(stylists);
            apType.setId(requestData.getAppointmentTypeID());
            List<AppointmentType> foundTypes = appointmentTypeRepository.findByCriteria(apType);
            if (foundTypes == null || foundTypes.isEmpty())
                throw new Exception("Appointment type is invalid for supplied stylist");

            requestData.setAppointmentType(foundTypes.get(0).getType());
        }

        // Force appointment status to NEW if it wasn't supplied
        if (requestData.getAppointmentStatus() == null)
            requestData.setAppointmentStatus(GenericModel.AppointmentStatus.NEW);

        // Appointment data is valid, insert into database
        GenericModel response = appointmentRepository.insert(requestData);
        Resource<GenericModel> resource = new Resource<>(response);
        resource.add(linkTo(AppointmentController.class).slash(response).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.ACCEPTED);
    }


    /**
     * Update an existing requestData
     * @param requestData requestData related information
     * @param appointmentID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{appointmentID}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<GenericModel>> editAppointment(@RequestBody Appointment requestData,
                                                                  @PathVariable String appointmentID,
                                                                  HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);

        // Validate request data and set update constraints
        Appointment targetAppointment = new Appointment();
        switch (this.authType)
        {
            case CLIENT:
                if (requestData.getAppointmentStatus() != null &&
                    requestData.getAppointmentStatus() != GenericModel.AppointmentStatus.CANCELED)
                {
                    throw new Exception("Clients can only change the appointment status to canceled.");
                }
                targetAppointment.setClientID(id);
                break;

            case STYLIST:
                targetAppointment.setStylistID(id);
                break;

            case ADMIN:
                break;

            default:
                break;
        }

        // Notify caller of update constraints
        if (requestData.getStartTime() != null || requestData.getEndTime() != null)
            throw new Exception("Start and end times cannot be changed. Cancel current appointment and make a new one");

        if (requestData.getStylistID() != null || requestData.getClientID() != null)
            throw new Exception("Assigned client or stylist cannot be changed.");

        if (requestData.getAppointmentType() != null || !requestData.getAppointmentType().isEmpty())
            throw new Exception("Different appointment types have different durations. Cancel current appointment and make a new one");


        // Attempt to find a single appointment with supplied id, constrained by caller authType
        targetAppointment.setId(appointmentID);
        List<Appointment> appointments = appointmentRepository.findByCriteria(targetAppointment);
        if (appointments == null || appointments.isEmpty())
            throw new Exception("Appointment with specified id was not found!");

        targetAppointment = appointments.get(0);

        // Appointment is found, check if caller is authorized to modify it
        switch (targetAppointment.getAppointmentStatus())
        {
            case NEW:
                break;

            case APPROVED:
                break;

            case COMPLETED:
                if (this.authType == GenericModel.UserType.STYLIST)
                    throw new Exception("Stylist cannot update COMPLETED appointments");

                break;

            default:
                // Restrict client initiated updates for other appointment statuses
                if (this.authType == GenericModel.UserType.CLIENT)
                    throw new Exception("Client cannot update an appointment if it's not NEW or APPROVED");
        }

        // Update target appointment with parameters supplied by requestData
        if (requestData.getAppointmentStatus() != null)
            targetAppointment.setAppointmentStatus(requestData.getAppointmentStatus());

        // TODO add more fields to the model

        appointmentRepository.save(targetAppointment);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}