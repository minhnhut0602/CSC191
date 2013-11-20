package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.model.AppointmentType;
import com.teamsierra.csc191.api.model.GenericModel;
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
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @Author: Alex Chernyak
 * @Date: 11/5/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.controller
 * @Description: Controller managing appointment types
 *
 * Public methods:
 * getAllTypes()
 * retrieve a list of all available appointment types and stylists assigned to these types
 *
 * getType(String)
 * get a specific appointment type specified by url
 *
 * addAppointmentType(addAppointmentType, HttpServletRequest)
 * add a new appointment type
 *
 * addStylistToType(String, String, HttpServletRequest)
 * add an existing stylist to an existing appointment type
 *
 * removeStylistFromType(String, String, HttpServletRequest)
 * remove a stylist from the appointment type
 */

@Controller
@RequestMapping(value = "/appointmentTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentTypeController extends GenericController
{
    @Autowired
    AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    public AppointmentTypeController(AppointmentTypeRepository atRepo)
    {
        this.appointmentTypeRepository = atRepo;
    }

    /**
     * Get all available appointment types and assigned stylists
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Resource<AppointmentType>>> getAllTypes() throws Exception
    {
        List<Resource<AppointmentType>> resources = new ArrayList<>();

        for (AppointmentType type: appointmentTypeRepository.findByCriteria(new AppointmentType()))
            resources.add(ResourceHandler.createResource(type));

        return new ResponseEntity<>(resources, HttpStatus.FOUND);
    }


    /**
     * Get details about specific appointment type
     * @param typeID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{typeID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource<AppointmentType>> getType(@PathVariable String typeID) throws Exception
    {
        AppointmentType findType = new AppointmentType();
        List<AppointmentType> foundTypes;

        if (typeID == null || typeID.isEmpty())
            throw new Exception("TypeID is not supplied!");

        findType.setId(typeID);

        foundTypes = appointmentTypeRepository.findByCriteria(findType);
        if (foundTypes == null || foundTypes.isEmpty())
            throw new Exception("No appointment type were found");

        Resource<AppointmentType> resource = ResourceHandler.createResource(foundTypes.get(0));

        return new ResponseEntity<>(resource, HttpStatus.FOUND);
    }


    /**
     * Add appointment type
     * @param requestData
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource<AppointmentType>> addAppointmentType(@RequestBody AppointmentType requestData,
                                                                        HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        AppointmentType findType = new AppointmentType();
        String type;

        switch (this.authType)
        {
            case ADMIN:
                // allow admins
                break;

            case CLIENT:
            case STYLIST:
            default:
                throw new Exception("This API call is forbidden");
        }
        type = requestData.getAppointmentType();

        if (type == null || type.isEmpty())
            throw new Exception("Valid appointment type name must be supplied");

        findType.setAppointmentType(type);

        if (!appointmentTypeRepository.findByCriteria(findType).isEmpty())
            throw new Exception("Appointment type already exists");

        requestData.setId(null);
        Resource<AppointmentType> resource = ResourceHandler.createResource(appointmentTypeRepository.insert(requestData));

        return new ResponseEntity<>(resource, HttpStatus.ACCEPTED);
    }


    /**
     * Add stylist to an existing appointment type
     * @param stylistID
     * @param typeID
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{typeID}/{stylistID}", method = RequestMethod.PUT)
    public ResponseEntity<Resource<GenericModel>> addStylistToType(@PathVariable String typeID,
                                                                   @PathVariable String stylistID,
                                                                   HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        List<AppointmentType> types = new ArrayList<>();
        AppointmentType findType = new AppointmentType();
        findType.setId(typeID);

        switch (this.authType)
        {
            case ADMIN:
                // admins are allowed to add stylists
                break;

            case CLIENT:
            case STYLIST:
            default:
                throw new Exception("This API call is forbidden");
        }

        // Validate request
        if (stylistID == null || stylistID.isEmpty())
            throw new Exception("Must provide a stylistID in the request body");
        if (typeID == null || typeID.isEmpty())
            throw new Exception("Must provide a typeId in url");

        // Validate db values
        if (this.userRepository.findById(stylistID) == null)
            throw new Exception("StylistID provided is invalid");

        types = appointmentTypeRepository.findByCriteria(findType);
        if (types == null || types.isEmpty())
            throw new Exception("Supplied typeID does not exist");

        for (String sid: types.get(0).getStylists())
        {
            if (stylistID.equalsIgnoreCase(sid))
                throw new Exception("Appointment type is already assigned to the stylist");
        }


        GenericModel response = new AppointmentType();
        response.setId(appointmentTypeRepository.addStylistToType(typeID, stylistID));
        Resource<GenericModel> resource = new Resource<>(response);
        resource.add(linkTo(AppointmentController.class).slash(response).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/{typeID}/{stylistID}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeStylistFromType(@PathVariable String typeID,
                                                        @PathVariable String stylistID,
                                                        HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        List<AppointmentType> types = new ArrayList<>();
        AppointmentType findType = new AppointmentType();
        findType.setId(typeID);

        switch (this.authType)
        {
            case ADMIN:
                // admins are allowed to add stylists
                break;

            case CLIENT:
            case STYLIST:
            default:
                throw new Exception("This API call is forbidden");
        }

        // Validate request
        if (stylistID == null || stylistID.isEmpty())
            throw new Exception("Must provide a stylistID in the request body");
        if (typeID == null || typeID.isEmpty())
            throw new Exception("Must provide a typeId in url");

        appointmentTypeRepository.deleteStylistFromType(typeID, stylistID);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
