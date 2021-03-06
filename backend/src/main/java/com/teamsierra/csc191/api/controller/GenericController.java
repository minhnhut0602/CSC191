package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.exception.GenericException;
import com.teamsierra.csc191.api.exception.UserAlreadyExistsException;
import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e)
    {
        // Return default status (500)
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof GenericException)
        {
            status = ((GenericException)e).getStatus();
        }
        else if (e instanceof UserAlreadyExistsException)
        {
            // do something
        }
        else
        {
            // Generic error
        }

        return new ResponseEntity<>(e.getMessage(), status);
    }


    /**
     * Set appropriate request attributes to be used in controller methods
     * @param request bla
     * @throws Exception
     */
    protected void setRequestControllerState(HttpServletRequest request) throws Exception
    {
        Object authToken = request.getAttribute("authToken");
        Object id = request.getAttribute("id");
        Object authType = request.getAttribute("authType");

        L.debug("authToken: " + authToken + " id: " + id + " authType: " + authType);

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


