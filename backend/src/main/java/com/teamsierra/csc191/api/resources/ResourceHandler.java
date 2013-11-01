package com.teamsierra.csc191.api.resources;

import com.teamsierra.csc191.api.controller.AppointmentController;
import com.teamsierra.csc191.api.controller.UserController;
import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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

        return resource;
    }

    public static Resource<User> createResource(User user)
    {
        Resource<User> resource;
        if (user != null)
            resource = new Resource<>(user);
        else
            return null;

        // generate links
        //resource.add(linkTo(AppointmentController.class).slash(user).withSelfRel());
        resource.add(linkTo(UserController.class).slash(user.getId()).withSelfRel());

        return resource;
    }

}
