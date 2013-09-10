package com.teamsierra.csc191.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * User: scott
 * Date: 9/9/13
 * Time: 4:49 PM
 */
@Controller
@RequestMapping("/")
public class AuthenticationController {

    @RequestMapping(method = RequestMethod.GET)
    public String loginView() {
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST);
    public String authenticate(HttpServletResponse response) {

    }

}
