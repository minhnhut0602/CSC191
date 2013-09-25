package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.exception.UserAlreadyExistsException;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addUser(@CookieValue(value = "authToken", required = false) String token)
           throws UserAlreadyExistsException/*, NotValidEmailException*/ {

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // TODO changePassword(@CookieValue(value = "authToken", required = false) String token)
    // TODO changeEmail(@CookieValue(value = "authToken", required = false) String token) throws NotValidEmailException
    // TODO changeName(@CookieValue(value = "authToken", required = false) String token)
    // TODO changePhoneNumber(@CookieValue(value = "authToken", required = false) String token) throws NotValidPhoneNumberException

    // TODO Exception handling

    @ExceptionHandler({})
    public ResponseEntity<String> notFound(Exception e) {
        String reason = String.format("\"reason\": \"%s\"", e.getMessage());
        return new ResponseEntity<String>(reason, HttpStatus.NOT_FOUND);
    }


}
