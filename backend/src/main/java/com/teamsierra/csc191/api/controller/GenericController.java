package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import java.util.Map;

/**
 * @Author: Alex Chernyak
 * @Date: 10/29/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.controller
 * @Description: place short description here
 */
public abstract class GenericController
{
    protected static final Log L = LogFactory.getLog(AppointmentController.class); // This logs out to STDOUT
    protected String authToken;
    protected String id;
    protected GenericModel.UserType authType;
    protected UserRepository userRepository;


    public void setAuthType(GenericModel.UserType authType)
    {
        this.authType = authType;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public void setUserRepository(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ModelAndView handleException(Exception e)
    {
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        ModelAndView reason = new ModelAndView(jsonView);
        reason.addObject("message", e.getMessage());
        reason.addObject("cause", e.getCause().toString());
        return reason;
    }
}


