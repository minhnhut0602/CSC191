package com.teamsierra.csc191.api.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import com.teamsierra.csc191.api.controller.AppointmentController;
import com.teamsierra.csc191.api.controller.AppointmentTypeController;
import com.teamsierra.csc191.api.controller.AvailabilityController;
import com.teamsierra.csc191.api.controller.UserController;
import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.AppointmentType;
import com.teamsierra.csc191.api.model.GenericModel.UserType;
import com.teamsierra.csc191.api.model.StylistAvailability;
import com.teamsierra.csc191.api.model.User;

/**
 * @Author: Alex Chernyak
 * @Date: 10/24/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.resources
 * @Description: resource handler for appointment and users classes to be used for HATEOAS
 */
public class ResourceHandler extends ResourceSupport
{
    public static Resource<Appointment> createResource(Appointment appointment)
    {
        Resource<Appointment> resource;
        if (appointment != null)
            resource = new Resource<>(appointment);
        else
            return null;

        // generate links
        resource.add(linkTo(AppointmentController.class).slash(appointment).withSelfRel());
        resource.add(linkTo(UserController.class).slash(appointment.getStylistID()).withRel("stylist"));
        resource.add(linkTo(UserController.class).slash(appointment.getClientID()).withRel("client"));
        resource.add(linkTo(AppointmentType.class).slash(appointment.getAppointmentTypeID()).withRel("appointmentType"));

        return resource;
    }
    
    /**
     * Creates a resource for a {@link User} with a self rel and a link to their
     * availability if they are a stylist or admin type user. 
     * 
     * @param user
     * @return
     */
    public static Resource<User> createResource(User user)
    {
        Resource<User> resource;
        if (user != null)
            resource = new Resource<>(user);
        else
            return null;

        // generate links
        resource.add(linkTo(UserController.class).slash(user.getId()).withSelfRel());
        if(user.getType() != UserType.CLIENT)
        {
        	resource.add(linkTo(AvailabilityController.class).slash(user.getId()).withRel("availability"));
        }

        return resource;
    }

    public static Resource<AppointmentType> createResource(AppointmentType type)
    {
        Resource<AppointmentType> resource;
        if (type != null)
            resource = new Resource<>(type);
        else
            return null;

        // generate links
        for (String stylistID : type.getStylists())
            resource.add(linkTo(UserController.class).slash(stylistID).withRel("stylist"));

        resource.add(linkTo(AppointmentTypeController.class).slash(type.getId()).withSelfRel());

        return resource;
    }
    
    /**
     * Generates a resource for a {@link StylistAvailability}. This resource contains
     * a link the the styilist's user as well as a link to their availability.
     * 
     * @param sa
     * @return
     */
    public static Resource<StylistAvailability> createResource(StylistAvailability sa)
    {
    	Resource<StylistAvailability> resource;
        if (sa != null)
            resource = new Resource<>(sa);
        else
            return null;

        // generate links
        resource.add(linkTo(UserController.class).slash(sa.getStylistID()).withRel("stylist"));
        resource.add(linkTo(AvailabilityController.class).slash(sa.getStylistID()).withRel("availability"));

        return resource;
    }
}
