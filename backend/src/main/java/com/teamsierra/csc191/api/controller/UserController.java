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
public class UserController{

    private static final Log L = LogFactory.getLog(UserController.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * A method to retrieve all of the users that the current user has access to.
     * 
     * @param headers requires headers "authType" and "authToken"
     * @return a List<User> 
     * @throws GenericUserException see message for why exception was thrown
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers(@RequestHeader Map<String, String> headers) throws GenericUserException
    {
    	List<User> users;
    	int authType;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get("authType"));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to getUsers().");
    	}
    	
    	try
    	{
    		switch(authType)
    		{
	    		case 0:	String authToken = headers.get("authToken");
						if(authToken == null)
					    {
					    	throw new GenericUserException("Unable to find authToken header. "
					    			+ "Exception generated in call to getUsers().");
					    }
						
						users = new ArrayList<User>();
						User user = userRepository.findByToken(authToken);
						if(user != null)
						{
							users.add(userRepository.findByToken(authToken));
						}
		    			
		    			break;
	    		case 1: users = userRepository.findAllActive();
	    				break;
	    		case 2: users = userRepository.findAll();
	    				break;
    			default: throw new GenericUserException("Unknown authType header. "
								+ "Exception generated in call to getUsers().");
    		}
	    	
	    	if(users != null && !users.isEmpty())
	    	{
	    		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	    	}
	    	else
	    	{
	    		throw new GenericUserException("No users found in the database. "
	    				+ "Exception generated in call to getUsers()");
	    	}
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Exception thrown while trying to retreive values from the userRepository. "
    				+ "Exception generated in call to getUsers().");
    	}
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addUser(@RequestHeader Map<String, String> headers,
                                        @RequestBody User user) throws GenericUserException 
    {
    	int authType;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get("authType"));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to addUsers().");
    	}
    	
    	try
    	{
	    	if(authType == 2)
	    	{
	    		//TODO validate
	    		userRepository.insert(user);
	    	}
	    	else
	    	{
	    		throw new GenericUserException("Admin credentials are required to create a new user. "
	    				+ "Exception generated in call to addUser().");
	    	}
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Exception thrown while trying to insert value into database. "
    				+ "Exception generated in call to addUser()");
    	}
    	
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable String userID,
    									@RequestHeader Map<String, String> headers) throws GenericUserException
    {
    	User user;
    	int authType;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get("authType"));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to getUser().");
    	}
    	
    	try
    	{
    		switch(authType)
    		{
	    		case 0:	String authToken = headers.get("authToken");
		    			if(authToken == null)
					    {
					    	throw new GenericUserException("Unable to find authToken header. "
					    			+ "Exception generated in call to getUser().");
					    }
		    			
		    			user = userRepository.findByToken(authToken);
		    			if(!user.getId().equals(userID))
		    			{
		    				throw new GenericUserException("A client only has access to their own user from the repository. "
		    						+ "Exception generated in call to getUser().");
		    			}
		    			
		    			break;
	    		case 1: user = userRepository.findById(userID);
	    				if(!user.isActive())
	    				{
	    					user = null;
	    				}
	    				break;
	    		case 2: user = userRepository.findById(userID);
	    				break;
    			default: throw new GenericUserException("Unknown authType header. "
    							+ "Exception generated in call to getUser().");
    		}
        	
    		if(user != null)
    		{
    			return new ResponseEntity<User>(user, HttpStatus.OK);
    		}
    		else
    		{
    			throw new GenericUserException("User not found in the database. "
    					+ "Exception generated in call to getUser()");
    		}
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Exception thrown while trying to retreive values from the userRepository. "
    				+ "Exception generated in call to getUser().");
    	}
    }
    
    @RequestMapping(value = "/{userID}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@PathVariable String userID,
    									   @RequestHeader Map<String, String> headers,
    									   @RequestBody User user) throws GenericUserException
    {
    	String authToken;
    	int authType;
    	User curUser;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get("authType"));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to updateUser().");
    	}
    	
    	try
    	{
    		if(!user.getId().equals(userID))
    		{
    			throw new GenericUserException("Mismatched IDs. The path variable userID does not match the user ID in the ResponseBody. "
    					+ "Exception generated in call to updateUser().");
    		}
    		
    		switch(authType)
    		{
	    		case 0:	authToken = headers.get("authToken");
		    			if(authToken == null)
					    {
					    	throw new GenericUserException("Unable to find authToken header. "
					    			+ "Exception generated in call to updateUser().");
					    }
		    			
		    			curUser = userRepository.findByToken(authToken);
	    				if(!curUser.getId().equals(userID))
		    			{
		    				throw new GenericUserException("A client only has access to their own user from the repository. "
		    						+ "Exception generated in call to updateUser().");
		    			}
		    			break;
		    			//TODO client un-editable fields
	    		case 1: authToken = headers.get("authToken");
		    			if(authToken == null)
					    {
					    	throw new GenericUserException("Unable to find authToken header. "
					    			+ "Exception generated in call to updateUser().");
					    }
		    			
		    			curUser = userRepository.findByToken(authToken);
	    				if(!curUser.getId().equals(userID))
		    			{
		    				throw new GenericUserException("A stylist only has access to their own user from the repository. "
		    						+ "Exception generated in call to updateUser().");
		    			}
	    				//TODO stylist un-editable fields
	    				break;
	    		case 2: //TODO validate the admin only fields 
	    				break;
    			default: throw new GenericUserException("Unknown authType header. "
    							+ "Exception generated in call to updateUser().");
    		}
    		
    		//TODO validate all other fields
    		userRepository.save(user);
        	return new ResponseEntity<Void>(HttpStatus.OK);
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Exception thrown while trying to update a user. "
    				+ "Exception generated in call to updateUser().");
    	}
    }
    
    @ExceptionHandler(GenericUserException.class)
    public ResponseEntity<String> handleGenericUserException(GenericUserException e)
    {
    	String reason = String.format("\"reason\": \"%s\"", e.getMessage());
    	return new ResponseEntity<String>(reason, e.getStatus());
    }

    // TODO Exception handling
    @ExceptionHandler({})
    public ResponseEntity<String> notFound(Exception e) 
    {
        String reason = String.format("\"reason\": \"%s\"", e.getMessage());
        return new ResponseEntity<String>(reason, HttpStatus.NOT_FOUND);
    }
}
