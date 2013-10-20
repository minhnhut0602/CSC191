package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/appointments")
public class AppointmentController {

    private static final Log L = LogFactory.getLog(AppointmentController.class); // This logs out to STDOUT

    @Autowired
    private AppointmentRepository appointmentRepository; // Database for dealing appointments.
    @Autowired
    private UserRepository userRepository; // User Database.

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) // Getting all appointments
    public ResponseEntity<List<Appointment>> getAppointments() {
        /*
        TODO Throw a generic exception because
        catching specific excpetions are "Stupid" accordint to Scott. :)
        */
        List<Appointment> appointments = appointmentRepository.findAll();

        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }

    @RequestMapping(value = "/{appointmentID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appointment> getAppointment(@CookieValue(value = "authToken", required = false) String token,
                                                      @PathVariable String appointmentID) {
        try {
            Appointment appointment = appointmentRepository.findByID(appointmentID);
            return new ResponseEntity<Appointment>(appointment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Appointment>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> addAppointment(@CookieValue(value = "authToken", required = false) String token,
                                               @RequestBody Appointment appointment) {
        User user = userRepository.findByToken(token);
        appointment.setClientID(user.getId());
        appointment = appointmentRepository.insert(appointment);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(AppointmentController.class).slash(appointment.getId()).toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }

    // TODO Edit Appointment()
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> editAppointment(@RequestBody Appointment put){
        // Incomplete
        // TODO
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}