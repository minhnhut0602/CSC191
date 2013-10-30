package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.resources.ResourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * User: scott
 * Date: 9/4/13
 * Time: 4:17 PM
 *
 * getAppointments()
 * getAppointment()
 * addAppointment
 * changeTime()
 * approveAppointment()
 * cancelAppointment()
 *
 */

@Controller
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentController extends GenericController
{
    @Autowired
    private AppointmentRepository appointmentRepository; // Database for dealing appointments.

    /**
     * Get list of all appointments relevant to a caller
     * @return list of appointments
     * @throws Exception
     * TODO date filter??
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Resource<Appointment>>> getAppointments() throws Exception
    {
        HttpStatus httpStatus;
        Appointment findAppointment = new Appointment();
        List<Resource<Appointment>> appointmentResources = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
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


        appointments = appointmentRepository.findByCriteria(findAppointment);

        if (appointments.isEmpty())
            httpStatus = HttpStatus.NOT_FOUND;
        else
            httpStatus = HttpStatus.FOUND;

        for(Appointment appointment: appointmentRepository.findByCriteria(findAppointment))
            appointmentResources.add(ResourceHandler.createResource(appointment));

        return new ResponseEntity<>(appointmentResources, httpStatus);
    }

    /**
     * Get Specific requestAppointment
     * @param appointmentID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{appointmentID}",  method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource<Appointment>> getAppointment(@PathVariable String appointmentID) throws Exception
    {
        Appointment findAppointment = new Appointment();
        Resource<Appointment> appointmentResource;
        List<Appointment> appointments;
        HttpStatus httpStatus;

        // setup request
        findAppointment.setId(appointmentID);
        switch (this.authType)
        {
            case CLIENT:
                findAppointment.setClientID(id);
            break;

            case STYLIST:
                findAppointment.setStylistID(id);
            break;

            case ADMIN:
            break;

            default:
            break;
        }

        appointments = appointmentRepository.findByCriteria(findAppointment);

        if (appointments == null || appointments.isEmpty())
            httpStatus = HttpStatus.NOT_FOUND;
        else if (appointments.size() > 1)
            httpStatus = HttpStatus.CONFLICT;
        else
            httpStatus = HttpStatus.FOUND;

        appointmentResource = ResourceHandler.createResource(appointments.get(0));

        return new ResponseEntity<>(appointmentResource, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/searchAppointments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Resource<Appointment>>> searchForAppointments(@RequestBody Appointment requestAppointment) throws Exception
    {
        List<Resource<Appointment>> appointmentResources = new ArrayList<>();
        List<Appointment> appointments;
        HttpStatus httpStatus;
        switch (this.authType)
        {
            case CLIENT:
                requestAppointment.setClientID(id);
            break;

            case STYLIST:
                requestAppointment.setStylistID(id);
            break;

            case ADMIN:
            break;

            default:
            break;
        }

        appointments = appointmentRepository.findByCriteria(requestAppointment);

        if (appointments == null || appointments.isEmpty())
            httpStatus = HttpStatus.NOT_FOUND;
        else
            httpStatus = HttpStatus.FOUND;


        for(Appointment appointment: appointments)
            appointmentResources.add(ResourceHandler.createResource(appointment));

        return new ResponseEntity<>(appointmentResources, httpStatus);
    }

    /**
     * Add requestAppointment
     * @param appointment
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Resource<GenericModel>> addAppointment(@RequestBody Appointment appointment) throws Exception
    {
        StringBuilder errors = new StringBuilder();
        switch (this.authType)
        {
            case CLIENT:
                appointment.setClientID(id);
            break;

            case STYLIST:
                appointment.setStylistID(id);
                break;

            case ADMIN:
            break;

            default:
            break;
        }

        // Check for missing fields
        if (appointment.getClientID().isEmpty())
            errors.append("clientID is missing ");
        if (appointment.getStylistID().isEmpty())
            errors.append("stylistID is missing ");
        if (appointment.getStartTime() == null)
            errors.append("startTime is missing ");
        if (appointment.getEndTime() == null)
            errors.append("endTime is missing ");

        if (errors.length() > 0)
            throw new Exception(errors.toString());

        if (appointment.getAppointmentStatus() == null)
            appointment.setAppointmentStatus(GenericModel.AppointmentStatus.NEW);

        // Validate stylist and availability
        errors.append(this.validateAvailability(appointment.getClientID(), appointment.getStylistID(),
                                                appointment.getStartTime(), appointment.getEndTime()));


        if (errors.length() > 0)
        {
            throw new Exception(errors.toString());
        }


        GenericModel response = appointmentRepository.insert(appointment);
        Resource<GenericModel> resource = new Resource<>(response);
        resource.add(linkTo(AppointmentController.class).slash(response).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.ACCEPTED);
    }

    /**
     * Update an existing appointment
     * @param appointment appointment related information
     * @param appointmentID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{appointmentID}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<GenericModel>> editAppointment(@RequestBody Appointment appointment,
                                                                  @PathVariable String appointmentID) throws Exception
    {
        Appointment findAppointment = new Appointment();
        switch (this.authType)
        {
            case CLIENT:
                if (appointment.getAppointmentStatus() != null &&
                    appointment.getAppointmentStatus() != GenericModel.AppointmentStatus.CANCELED)
                {
                    throw new Exception("Clients can only change the appointment status to canceled.");
                }
                appointment.setClientID(id);
                findAppointment.setClientID(id);
                break;

            case STYLIST:
                appointment.setStylistID(id);
                findAppointment.setStylistID(id);
                break;

            case ADMIN:
                break;

            default:
                break;
        }

        if ((appointment.getStartTime() != null) ^ (appointment.getEndTime() != null))
        {
            throw new Exception("Time range is missing start or an end");
        }

        findAppointment.setId(appointmentID);

        List<Appointment> appointments = appointmentRepository.findByCriteria(findAppointment);

        if (appointments == null || appointments.isEmpty())
        {
            throw new Exception("Specified appointment not found!");
        }

        findAppointment = appointments.get(0);
        GenericModel.AppointmentStatus status = findAppointment.getAppointmentStatus();

        if (this.authType == GenericModel.UserType.CLIENT &&
            status != GenericModel.AppointmentStatus.APPROVED &&
            status != GenericModel.AppointmentStatus.NEW)
        {
            throw new Exception("Client cannot update an appointment if it's not NEW or APPROVED");
        }
        if (this.authType == GenericModel.UserType.STYLIST &&
            status == GenericModel.AppointmentStatus.COMPLETED)
        {
            throw new Exception("Stylist cannot update COMPLETED appointments");
        }

        // TODO add more fields to the model
        if (appointment.getAppointmentStatus() != null)
            findAppointment.setAppointmentStatus(appointment.getAppointmentStatus());

        appointmentRepository.save(findAppointment);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private String validateAvailability(String clientID, String stylistID, Date startTime, Date endTime)
    {
        Appointment findAppointment = new Appointment();
        List<Appointment> appointments = new ArrayList<>();
        findAppointment.setStartTime(startTime);
        findAppointment.setEndTime(endTime);

        if (!clientID.isEmpty())
        {
            User client = userRepository.findById(clientID);
            if (client == null)
            {
                return "Invalid client provided ";
            }
            findAppointment.setClientID(clientID);
            appointments.addAll(appointmentRepository.findByCriteria(findAppointment));
        }

        if (!stylistID.isEmpty())
        {
            User stylist = userRepository.findById(stylistID);
            if (stylist == null)
            {
                return "Invalid stylist provided";
            }
            findAppointment.setStylistID(stylistID);
            appointments.addAll(appointmentRepository.findByCriteria(findAppointment));
        }


        for (Appointment appointment: appointments)
        {
            if (appointment.getAppointmentStatus() == GenericModel.AppointmentStatus.APPROVED)
            {
                return "Given date/time range conflicts with accepted appointments";
            }
        }

        return null;
    }
}