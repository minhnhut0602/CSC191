package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.interceptor.AuthInterceptor;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * User: scott
 * Date: 9/14/13
 * Time: 10:21 AM
 */

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Log L = LogFactory.getLog(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthInterceptor authInterceptor;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addUser(@RequestHeader Map<String, String> headers,
                                        @RequestBody   Map<String, String> post) {

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // TODO Exception handling

    @ExceptionHandler({})
    public ResponseEntity<String> notFound(Exception e) {
        String reason = String.format("\"reason\": \"%s\"", e.getMessage());
        return new ResponseEntity<String>(reason, HttpStatus.NOT_FOUND);
    }


}
