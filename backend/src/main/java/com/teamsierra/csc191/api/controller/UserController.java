package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.exception.GenericUserException;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.UserRepository;
import com.teamsierra.csc191.api.resources.ResourceHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
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
    
    //headers
    private static final String AUTH_TYPE = "authtype";
    private static final String AUTH_TOKEN = "authtoken"; 

    /**
     * A method to retrieve all of the users that the current user has access to.
     * 
     * @param headers requires headers "authType" and "authToken"
     * @return a List<User> 
     * @throws GenericUserException see message for why exception was thrown
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Resource<User>>> getUsers(@RequestHeader Map<String, String> headers) throws GenericUserException
    {
    	List<User> users;
    	int authType;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get(AUTH_TYPE));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to getUsers().", HttpStatus.BAD_REQUEST);
    	}
    	
		switch(authType)
		{
    		case 0:	String authToken = headers.get(AUTH_TOKEN);
					if(authToken == null)
				    {
				    	throw new GenericUserException("Unable to find authToken header. "
				    			+ "Exception generated in call to getUsers().", HttpStatus.BAD_REQUEST);
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
							+ "Exception generated in call to getUsers().", HttpStatus.BAD_REQUEST);
		}
    	
    	if(users != null && !users.isEmpty())
    	{
    		List<Resource<User>> resource = new ArrayList<Resource<User>>();
    		for(User u : users)
    		{
    			resource.add(ResourceHandler.createResource(u));
    		}
    		
    		return new ResponseEntity<List<Resource<User>>>(resource, HttpStatus.OK);
    	}
    	else
    	{
    		throw new GenericUserException("No users found in the database. "
    				+ "Exception generated in call to getUsers()", HttpStatus.NOT_FOUND);
    	}
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<User>> addUser(@RequestHeader Map<String, String> headers,
                                        		  @RequestBody User user) throws GenericUserException 
    {
    	int authType;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get(AUTH_TYPE));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to addUsers().", HttpStatus.BAD_REQUEST);
    	}
    	
    	try
    	{
	    	if(authType == 2)
	    	{
	    		//TODO validate password
	    		// required fields
	    		if(!isValidGroup(user.getGroup()))
	    		{
	    			throw new GenericUserException("Invalid group number. Group should be between 1 and 2. "
	    					+ "Exception generated in call to addUser().", HttpStatus.BAD_REQUEST);
	    		}
	    		if(user.getFirstName() == null || !isValidName(user.getFirstName()))
	    		{
	    			throw new GenericUserException("Invalid first name. Names should consist of only unicode letters. "
	    					+ "Exception generated in call to addUser().", HttpStatus.BAD_REQUEST);
	    		}
	    		if(user.getLastName() == null || !isValidName(user.getLastName()))
	    		{
	    			throw new GenericUserException("Invalid last name. Names should consist of only unicode letters. "
	    					+ "Exception generated in call to addUser().", HttpStatus.BAD_REQUEST);
	    		}
	    		if(user.getEmail() == null || !isValidEmail(user.getEmail()))
	    		{
	    			throw new GenericUserException("Invalid email. "
	    					+ "Exception generated in call to addUser().", HttpStatus.BAD_REQUEST);
	    		}
	    		//optional fields
	    		if(user.getAvatarURL() != null)
	    		{
		    		if(!isValidAvatarURL(user.getAvatarURL()))
		    		{
		    			throw new GenericUserException("Invalid avatarURL. The avatarURL should be an image URL ending in .png, .jpg, or .gif. "
		    					+ "Exception generated in call to addUser().", HttpStatus.BAD_REQUEST);
		    		}
	    		}
	    		if(user.getPhone() != null)
	    		{
		    		if(!isValidPhoneNumber(user.getPhone()))
		    		{
		    			throw new GenericUserException("Invalid phone number. The phone number should consist of 10 digits. "
		    					+ "Exception generated in call to addUser().", HttpStatus.BAD_REQUEST);
		    		}
	    		}
	    		userRepository.insert(user);
	    		Resource<User> resource = ResourceHandler.createResource(user);
	    		return new ResponseEntity<Resource<User>>(resource, HttpStatus.CREATED);
	    	}
	    	else
	    	{
	    		throw new GenericUserException("Admin credentials are required to create a new user. "
	    				+ "Exception generated in call to addUser().", HttpStatus.FORBIDDEN);
	    	}
    	}
    	catch(GenericUserException gue)
    	{
    		throw gue;
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Exception thrown while trying to insert value into database. "
    				+ "Exception generated in call to addUser()", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @RequestMapping(value = "/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<User>> getUser(@PathVariable String userID,
    											  @RequestHeader Map<String, String> headers) throws GenericUserException
    {
    	User user;
    	int authType;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get(AUTH_TYPE));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to getUser().", HttpStatus.BAD_REQUEST);
    	}
    	
    	try
    	{
    		switch(authType)
    		{
	    		case 0:	String authToken = headers.get(AUTH_TOKEN);
		    			if(authToken == null)
					    {
					    	throw new GenericUserException("Unable to find authToken header. "
					    			+ "Exception generated in call to getUser().", HttpStatus.BAD_REQUEST);
					    }
		    			
		    			user = userRepository.findByToken(authToken);
		    			if(!user.getId().equals(userID))
		    			{
		    				throw new GenericUserException("A client only has access to their own user from the repository. "
		    						+ "Exception generated in call to getUser().", HttpStatus.FORBIDDEN);
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
    							+ "Exception generated in call to getUser().", HttpStatus.BAD_REQUEST);
    		}
        	
    		if(user != null)
    		{
    			return new ResponseEntity<Resource<User>>(ResourceHandler.createResource(user), HttpStatus.OK);
    		}
    		else
    		{
    			throw new GenericUserException("User not found in the database. "
    					+ "Exception generated in call to getUser().", HttpStatus.NOT_FOUND);
    		}
    	}
    	catch(GenericUserException gue)
    	{
    		throw gue;
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Exception thrown while trying to retreive values from the userRepository. "
    				+ "Exception generated in call to getUser().", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @RequestMapping(value = "/{userID}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<User>> updateUser(@PathVariable String userID,
    									   			 @RequestHeader Map<String, String> headers,
    									   			 @RequestBody User user) throws GenericUserException
    {
    	String authToken;
    	int authType;
    	User curUser;
    	
    	try
    	{
    		authType = Integer.parseInt(headers.get(AUTH_TYPE));
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Unable to resolve authType header to type int. "
	    			+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
    	}
    	
    	try
    	{
    		switch(authType)
    		{
	    		case 0:	authToken = headers.get(AUTH_TOKEN);
		    			if(authToken == null)
					    {
					    	throw new GenericUserException("Unable to find authToken header. "
					    			+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
					    }
		    			
		    			curUser = userRepository.findByToken(authToken);
	    				if(!curUser.getId().equals(userID))
		    			{
		    				throw new GenericUserException("A client only has access to their own user from the repository. "
		    						+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
		    			}
	    				// update client specific editable fields
	    				// currently none
		    			break;
	    		case 1: authToken = headers.get(AUTH_TOKEN);
		    			if(authToken == null)
					    {
					    	throw new GenericUserException("Unable to find authToken header. "
					    			+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
					    }
		    			
		    			curUser = userRepository.findByToken(authToken);
	    				if(!curUser.getId().equals(userID))
		    			{
		    				throw new GenericUserException("A stylist only has access to their own user from the repository. "
		    						+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
		    			}
	    				// update stylist specific editable fields
	    				updateStylist(user, curUser);
	    				break;
	    		case 2: curUser = userRepository.findById(userID);
						
						if(curUser.getGroup() != "CLIENT")
						{
							// currently, admin and stylist editable fields are the same
							updateStylist(user, curUser);
						}
						
						curUser.setActive(user.isActive());
	    				break;
    			default: throw new GenericUserException("Unknown authType header. "
    							+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
    		}
    		
    		// editable fields for all users
    		if(user.getPhone() != null)
    		{
	    		if(isValidPhoneNumber(user.getPhone()))
	    		{
	    			curUser.setPhone(user.getPhone());
	    		}
	    		else
	    		{
	    			throw new GenericUserException("Invalid phone number. The phone number should consist of 10 digits. "
	    					+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
	    		}
    		}
    		
    		userRepository.save(curUser);
        	return new ResponseEntity<Resource<User>>(ResourceHandler.createResource(user), HttpStatus.ACCEPTED);
    	}
    	catch(GenericUserException gue)
    	{
    		throw gue;
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException("Exception thrown while trying to update a user. "
    				+ "Exception generated in call to updateUser().", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    private void updateStylist(User user, User curUser) throws GenericUserException
    {
    	if(user.getFirstName() != null)
		{
			if(isValidName(user.getFirstName()))
			{
				curUser.setFirstName(user.getFirstName());
			}
			else
			{
				throw new GenericUserException("Invalid first name. Names should consist of only unicode letters. "
    					+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
			}
		}
		if(user.getLastName() != null)
		{
			if(isValidName(user.getLastName()))
			{
				curUser.setLastName(user.getLastName());
			}
			else
			{
				throw new GenericUserException("Invalid last name. Names should consist of only unicode letters. "
    					+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
			}
		}
		if(user.getEmail() != null)
		{
			if(isValidEmail(user.getEmail()))
			{
				curUser.setEmail(user.getEmail());
			}
			else
			{
				throw new GenericUserException("Invalid email. "
    					+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
			}
		}
		if(user.getAvatarURL() != null)
		{
			if(isValidAvatarURL(user.getAvatarURL()))
    		{
    			curUser.setAvatarURL(user.getAvatarURL());	
    		}
			else
			{
				throw new GenericUserException("Invalid avatarURL. The avatarURL should be an image URL ending in .png, .jpg, or .gif. "
    					+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
			}
		}
    }
    
    private boolean isValidGroup(String group)
    {
        // TODO fix group type to usertype enum
    	return true;
    }
    
    private boolean isValidName(String name)
    {
    	return name.matches("^\\p{L}+$"); // one or more of any unicode character
    }
    
    private boolean isValidEmail(String email)
    {
    	return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }
    
    private boolean isValidAvatarURL(String avatarURL)
    {
    	return avatarURL.matches("[^\\s]+(\\.(?i)(png|gif|jpg))$"); // one or more characters followed by .png, .gif, or .jpg
    }
    
    private boolean isValidPhoneNumber(String phoneNumber)
    {
    	return phoneNumber.matches("^\\d{10}$"); // 10 digits
    }
    
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> handleTypeMisMatchException(TypeMismatchException e) throws GenericUserException
    {
    	StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String stackTrace = "Stack Trace: " + e.toString();
        for(StackTraceElement te : ste)
        {
        	stackTrace += te + "\n";
        }
    	throw new GenericUserException("TypeMismatchException occured. Likely while trying to convert the request body into type User. " 
    			+ "With message: " + e.getMessage() + ".\n\n" + stackTrace, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(GenericUserException.class)
    public ResponseEntity<String> handleGenericUserException(GenericUserException e)
    {
    	String reason = String.format("\"reason\": \"%s\"", e.getMessage());
    	return new ResponseEntity<String>(reason, e.getStatus());
    }

    // TODO Exception handling
    /*@ExceptionHandler()
    public ResponseEntity<String> notFound(Exception e) 
    {
    	StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String reason = String.format("\"reason\": \"%s\" " + e.toString() + "\n", e.getMessage());
        for(StackTraceElement te : ste)
        {
        	reason += te + "\n";
        }
        return new ResponseEntity<String>(reason, HttpStatus.NOT_FOUND);
    }*/
}
