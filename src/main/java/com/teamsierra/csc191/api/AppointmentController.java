package com.teamsierra.csc191.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: scott
 * Date: 9/4/13
 * Time: 4:17 PM
 *
 */

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getAppointments() {
        return "{'test':true}";
    }

}
