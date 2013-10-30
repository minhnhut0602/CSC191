package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.exception.GenericUserException;
import com.teamsierra.csc191.api.interceptor.AuthInterceptor;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.UserRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers(@RequestHeader Map<String, String> headers) throws GenericUserException{
    	List<User> users;
    	
    	String authType = headers.get("authType");
	    String authToken = headers.get("authToken");
	    if(authType == null || authToken == null){
	    	throw new GenericUserException("Unable to find headers in call to getUsers().");
	    }
    	
    	try{
	    	if(authType.equals("admin")){
	    		users = userRepository.findAll();
	    	}
	    	else{
	    		if(authType.equals("stylist")){
	    			users = userRepository.findAllActive();
	    		}
	    		else{
	    			if(authType.equals("client"))
	    			{
	    				users = new ArrayList<User>();
	    				users.add(userRepository.findByToken(authToken));
	    			}
	    			else{
	    				throw new GenericUserException("Unknown authType in call to getUsers().");
	    			}
	    		}    		
	    	}
	    	
	    	return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    	
    	}catch(Exception e){
    		throw new GenericUserException("Exception thrown while trying to retreive values from the userRepository in call to getUsers().");
    	}
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addUser(@RequestHeader Map<String, String> headers,
                                        @RequestBody   Map<String, String> post) {
    	//TODO
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("userID") String userID,
    									@RequestHeader Map<String, String> headers) throws GenericUserException{
    	User user;
    	
	    String authType = headers.get("authType");
	    String authToken = headers.get("authToken");
	    if(authType == null || authToken == null){
	    	throw new GenericUserException("Unable to find headers in call to getUsers().");
	    }
    	
    	try{
    		if(authType.equals("client")){
    			User client = userRepository.findByToken(authToken);
    			if(!client.getId().equals(userID)){
    				throw new GenericUserException("Client tried to retrieve a different user from the repository in call to getUser().");
    			}
    		}
    		
    		user = userRepository.findById(userID);
        	
        	return new ResponseEntity<User>(user, HttpStatus.OK);
    	}catch(Exception e){
    		throw new GenericUserException("Exception thrown while trying to retreive values from the userRepository in call to getUser().");
    	}
    }
    
    @RequestMapping(value = "/{userID}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@PathVariable("userID") String userID){
    	User user = userRepository.findById(userID);
    	//TODO make changes
    	userRepository.save(user);
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // TODO Exception handling
    @ExceptionHandler({})
    public ResponseEntity<String> notFound(Exception e) {
        String reason = String.format("\"reason\": \"%s\"", e.getMessage());
        return new ResponseEntity<String>(reason, HttpStatus.NOT_FOUND);
    }
}
