package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User: scott
 * Date: 9/4/13
 * Time: 4:17 PM
 *
 */

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getAppointments(HttpServletRequest request) {
        request.getCookies();
        User user = userRepository.findByToken();
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.toString();
    }

}
