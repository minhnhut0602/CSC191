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

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public HashMap<String, String> handleException(Exception e)
    {
        HashMap<String, String> error = new HashMap<>();
        error.put("details",e.toString());
        error.put("error", e.getMessage());
        return error;
    }


    /**
     * Set appropriate request attributes to be used in controller methods
     * @param request bla
     * @throws Exception
     */
    protected void setRequestControllerState(HttpServletRequest request) throws Exception
    {
        Object authToken = null;
        Object id = null;
        Object authType = null;

        authToken = request.getAttribute("authToken").toString();
        id = request.getAttribute("id");
        authType = request.getAttribute("authType");

        if (authToken == null || authToken.toString().isEmpty())
            throw new Exception("authToken is missing from the request");
        if (id == null || id.toString().isEmpty())
            throw new Exception("id is missing from the request");
        if (authType == null || authType.toString().isEmpty())
            throw new Exception("authType is missing from the request");

        this.authToken = authToken.toString();
        this.id = id.toString();
        this.authType = GenericModel.UserType.valueOf(authType.toString().toUpperCase());
    }
}


