package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Alex Chernyak
 * @Date: 10/29/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.controller
 * @Description: place short description here
 */
public abstract class GenericController
{
    protected static final Log L = LogFactory.getLog(GenericController.class); // This logs out to STDOUT
    protected String authToken;
    protected String id;
    protected GenericModel.UserType authType;

    @Autowired
    protected UserRepository userRepository;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception e)
    {
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        ModelAndView reason = new ModelAndView(jsonView);
        reason.addObject("message", e.getMessage());
        reason.addObject("cause", e.getCause().toString());
        return reason;
    }

    protected void setRequestControllerState(HttpServletRequest request)
    {
        this.authToken = request.getAttribute("authToken").toString();
        this.id = request.getAttribute("id").toString();
        this.authType = GenericModel.UserType.valueOf(request.getAttribute("authType").toString());
    }
}


