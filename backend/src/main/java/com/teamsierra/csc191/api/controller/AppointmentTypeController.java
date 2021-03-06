package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.exception.GenericException;
import com.teamsierra.csc191.api.model.AppointmentType;
import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.model.GenericModel.UserType;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentTypeRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
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
 * getType(String id)
 * get a specific appointment type specified by id in url
 * Sample returned object:
 * {
 *   "links": [
 *     {
 *       "rel": "self",
 *       "href": "http://10.100.1.6:8080/salon/appointmentTypes/527b558ee4b01219435d812b"
 *     }
 *   ],
 *   "id": "527b558ee4b01219435d812b",
 *   "appointmentType": "color",
 *   "durationInMinutes": 0,
 *   "basePrice": 0,
 *   "stylists": []
 * }
 *
 * addAppointmentType(AppointmentType requestData, HttpServletRequest)
 * add a new appointment type
 * Sample requestData:
 * {
 *   "appointmentType": "color",
 *   "durationInMinutes": 0,
 *   "basePrice": 0
 * }
 *
 * addStylistToType(String typeid, String stylistid, HttpServletRequest)
 * add an existing stylist to an existing appointment type
 *
 * removeStylistFromType(String typeid, String stylistid, HttpServletRequest)
 * remove a stylist from the appointment type
 */

@Controller
@RequestMapping(value = "/appointmentTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentTypeController extends GenericController
{
    @Autowired
    AppointmentTypeRepository appointmentTypeRepository;
    UserRepository userRepository;

    @Autowired
    public AppointmentTypeController(AppointmentTypeRepository atRepo,
    								 UserRepository userRepo)
    {
        this.appointmentTypeRepository = atRepo;
        this.userRepository = userRepo;
    }

    /**
     * Get all available appointment types and assigned stylists
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Resource<AppointmentType>>> getAllTypes(HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        List<Resource<AppointmentType>> resources = new ArrayList<>();

        for (AppointmentType type: appointmentTypeRepository.findByCriteria(new AppointmentType()))
        {
            resources.add(ResourceHandler.createResource(type, this.id));
        }

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }



    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Resource<AppointmentType>>> getStylistTypes(@RequestParam String stylistID,
                                                                           HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);

        List<Resource<AppointmentType>> resources = new ArrayList<>();
        AppointmentType findType = new AppointmentType();
        String[] stylists = new String[]{stylistID};
        findType.setStylists(stylists);

        for (AppointmentType type: appointmentTypeRepository.findByCriteria(findType))
            resources.add(ResourceHandler.createResource(type, this.id));

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }


    /**
     * Get details about specific appointment type
     * @param typeID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{typeID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource<AppointmentType>> getType(@PathVariable String typeID,
                                                             HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        AppointmentType findType = new AppointmentType();
        List<AppointmentType> foundTypes;

        if (typeID == null || typeID.isEmpty())
            throw new GenericException("TypeID is not supplied", HttpStatus.BAD_REQUEST, L);

        findType.setId(typeID);

        foundTypes = appointmentTypeRepository.findByCriteria(findType);
        if (foundTypes == null || foundTypes.isEmpty())
            throw new GenericException("Appointment type was not found", HttpStatus.NOT_FOUND, L);

        Resource<AppointmentType> resource = ResourceHandler.createResource(foundTypes.get(0), this.id);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


    /**
     * Delete existing appointment type
     * @param typeID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{typeID}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Object> deleteType(@PathVariable String typeID,
                                             HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        AppointmentType findType = new AppointmentType();
        List<AppointmentType> foundTypes;

        switch (this.authType)
        {
            case ADMIN:
                // admins and stylists are allowed to remove stylists from types
                break;

            case CLIENT:
            case STYLIST:
            default:
                throw new GenericException("This API call is forbidden", HttpStatus.FORBIDDEN, L);
        }

        if (typeID == null || typeID.isEmpty())
            throw new GenericException("TypeID is not supplied", HttpStatus.BAD_REQUEST, L);

        findType.setId(typeID);
        foundTypes = appointmentTypeRepository.findByCriteria(findType);
        if (foundTypes == null || foundTypes.isEmpty())
            throw new GenericException("Appointment type was not found", HttpStatus.NOT_FOUND, L);

        // Delete type
        appointmentTypeRepository.deleteType(typeID);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
                throw new GenericException("Permission denied for this API call", HttpStatus.UNAUTHORIZED, L);
        }
        type = requestData.getAppointmentType();

        if (type == null || type.isEmpty())
            throw new GenericException("Valid appointment type name must be supplied", HttpStatus.BAD_REQUEST, L);

        findType.setAppointmentType(type);

        if (!appointmentTypeRepository.findByCriteria(findType).isEmpty())
            throw new GenericException("Appointment type already exists", HttpStatus.CONFLICT, L);

        requestData.setId(null);
        Resource<AppointmentType> resource = ResourceHandler.createResource(appointmentTypeRepository.insert(requestData),
                                                                            this.id);

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
    @ResponseBody
    public ResponseEntity<Resource<GenericModel>> addStylistToType(@PathVariable String typeID,
                                                                   @PathVariable String stylistID,
                                                                   HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        List<AppointmentType> types = new ArrayList<>();
        AppointmentType findType = new AppointmentType();
        GenericModel response = new AppointmentType();
        Boolean isDuplicate = false;
        response.setId(typeID);
        findType.setId(typeID);

        switch (this.authType)
        {
            case ADMIN:
            case STYLIST:
                // admins and stylists are allowed to add stylists to types
                break;

            case CLIENT:
            default:
                throw new GenericException("Permission denied for this API call", HttpStatus.UNAUTHORIZED, L);
        }

        // Validate request
        if (stylistID == null || stylistID.isEmpty())
            throw new GenericException("Must provide a stylistID in the request body", HttpStatus.BAD_REQUEST, L);
        if (typeID == null || typeID.isEmpty())
            throw new GenericException("Must provide a typeId in url", HttpStatus.BAD_REQUEST, L);

        // Validate db values
        if (this.userRepository.findById(stylistID) == null)
            throw new GenericException("StylistID provided is invalid", HttpStatus.NOT_FOUND, L);

        types = appointmentTypeRepository.findByCriteria(findType);
        if (types == null || types.isEmpty())
            throw new GenericException("Supplied typeID does not exist", HttpStatus.NOT_FOUND, L);

        for (String sid: types.get(0).getStylists())
        {
            if (stylistID.equalsIgnoreCase(sid))
            {
                isDuplicate = true;

                //throw new GenericException("Appointment type is already assigned to the stylist",
                //                           HttpStatus.CONFLICT, L);
            }
        }


        if (!isDuplicate)
        {
            response.setId(appointmentTypeRepository.addStylistToType(typeID, stylistID));
        }
        Resource<GenericModel> resource = new Resource<>(response);
        resource.add(linkTo(AppointmentTypeController.class).slash(response).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/{typeID}/{stylistID}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Resource<GenericModel>> removeStylistFromType(@PathVariable String typeID,
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
            case STYLIST:
                // admins and stylists are allowed to remove stylists from types
                break;

            case CLIENT:
            default:
                throw new GenericException("This API call is forbidden", HttpStatus.FORBIDDEN, L);
        }

        // Validate request
        if (stylistID == null || stylistID.isEmpty())
            throw new GenericException("Must provide a stylistID in the request body", HttpStatus.BAD_REQUEST, L);
        if (typeID == null || typeID.isEmpty())
            throw new GenericException("Must provide a typeId in url", HttpStatus.BAD_REQUEST, L);

        appointmentTypeRepository.deleteStylistFromType(typeID, stylistID);
        GenericModel response = new AppointmentType();
        response.setId(typeID);
        Resource<GenericModel> resource = new Resource<>(response);
        resource.add(linkTo(AppointmentTypeController.class).slash(response).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.ACCEPTED);
    }


    /**
     * This is a stupid hack requested by Mr. Jabbari because he was dam too lazy
     * to actually call two different methods from the front end. He owes me a COFFEE!
     * @param typeID
     * @param add
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{typeID}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Resource<GenericModel>> modifyTypeStylist(@PathVariable String typeID,
                                                                    @RequestParam Boolean add,
                                                                    HttpServletRequest request) throws Exception
    {
        this.setRequestControllerState(request);
        String stylistID = this.id;

        if (add)
        {
           return addStylistToType(typeID, stylistID, request);
        }
        else
        {
           return removeStylistFromType(typeID, stylistID, request);
        }
    }
}
