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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


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
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public HashMap<String, String> handleException(Exception e)
    {
        HashMap<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return error;
    }

    /**
     * Set appropriate request attributes to be used in controller methods
     * @param request
     */
    protected void setRequestControllerState(HttpServletRequest request)
    {
        this.authToken = request.getAttribute("authToken").toString();
        this.id = request.getAttribute("id").toString();
        this.authType = GenericModel.UserType.valueOf(request.getAttribute("authType").toString());
    }
}


