package com.teamsierra.csc191.api.controller;

import com.lambdaworks.crypto.SCryptUtil;
import com.teamsierra.csc191.api.exception.GenericException;
import com.teamsierra.csc191.api.model.Authentication;
import com.teamsierra.csc191.api.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * User: scott
 * Date: 9/9/13
 * Time: 4:49 PM
 */
@Controller
@RequestMapping("/authorize")
public class AuthenticationController extends GenericController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Authentication> loginView(@RequestParam(value = "username") String username,
                                                    @RequestParam(value = "password") String password)
                                                    throws Exception {

        User user = userRepository.findByEmail(username);
        // keep inactive users from logging in
     	if(!user.isActive())
     	{
     		throw new GenericException("This user has been deactivated by an admin.",
     				HttpStatus.UNAUTHORIZED, L);
     	}
        if (SCryptUtil.check(password, user.getPassword())) {
            SecureRandom random = new SecureRandom();
            String token = new BigInteger(512, random).toString(64);
            Authentication auth = new Authentication();
            auth.setAuthToken(token);
            user.setToken(token);
            userRepository.save(user);
            return new ResponseEntity<>(auth, HttpStatus.ACCEPTED);
        } else {
            throw new GenericException("username and password do not match", HttpStatus.UNAUTHORIZED, L);
        }
    }

}
