package com.teamsierra.csc191.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.teamsierra.csc191.api.exception.GenericUserException;
import com.teamsierra.csc191.api.model.GenericModel.UserType;
import com.teamsierra.csc191.api.model.StylistAvailability;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
import com.teamsierra.csc191.api.resources.ResourceHandler;

/**
 * User: scott
 * Date: 9/14/13
 * Time: 10:21 AM
 */

@Controller
@RequestMapping("/users")
public class UserController extends GenericController
{
    private static final Log L = LogFactory.getLog(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StylistAvailabilityRepository sar;

    /**
     * A method to retrieve all of the users that the current user has access to.
     * 
     * @param headers requires headers "authType" and "authToken"
     * @return a List<User> 
     * @throws GenericUserException see message for why exception was thrown
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Resource<User>>> getUsers(HttpServletRequest request) throws GenericUserException
    {
    	L.info("GET called at path /.");
    	try
    	{
			this.setRequestControllerState(request);
		} 
    	catch (Exception e) 
    	{
			throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    	
    	List<User> users = null;
    	
    	try
    	{
			switch(authType)
			{
	    		case CLIENT: users = new ArrayList<User>();
							 User user = userRepository.findByToken(authToken);
							 if(user != null)
							 {
								users.add(userRepository.findByToken(authToken));
							 }
			    			
			    			 break;
	    		case STYLIST: users = userRepository.findAllActive();
	    					  break;
	    		case ADMIN: users = userRepository.findAll();
	    					break;
				default: throw new GenericUserException("Unknown authType header. "
								+ "Exception generated in call to getUsers().", HttpStatus.BAD_REQUEST);
			}
    	}
		catch(Exception e)
		{
			throw new GenericUserException("Unknown authType header. "
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
    				+ "Exception generated in call to getUsers()", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<User>> addUser(HttpServletRequest request,
                                        		  @RequestBody User user) throws GenericUserException 
    {
    	L.info("POST called at path /.");
    	try
    	{
			this.setRequestControllerState(request);
		} 
    	catch (Exception e) 
    	{
			throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    	
    	try
    	{
	    	if(authType == UserType.ADMIN)
	    	{
	    		//TODO password stuff
	    		// required fields
	    		String error = "";
	    		if(!isValidGroup(user.getGroup()))
	    		{
	    			error  += "Invalid group number. Group should be either stylist or admin.\n";
	    		}
	    		if(user.getFirstName() == null || !isValidName(user.getFirstName()))
	    		{
	    			error += "Invalid first name. Names should consist of only unicode letters.\n";
	    		}
	    		if(user.getLastName() == null || !isValidName(user.getLastName()))
	    		{
	    			error += "Invalid last name. Names should consist of only unicode letters.\n";
	    		}
	    		if(user.getEmail() == null || !isValidEmail(user.getEmail()))
	    		{
	    			error += "Invalid email.\n";
	    		}
	    		if(user.getPassword() == null || !isValidPassword(user.getPassword()))
	    		{
	    			error += "Invalid password.\n";
	    		}
	    		//optional fields
	    		if(user.getAvatarURL() != null)
	    		{
		    		if(!isValidAvatarURL(user.getAvatarURL()))
		    		{
		    			error += "Invalid avatarURL. The avatarURL should be an image URL ending in .png, .jpg, or .gif.\n";
		    		}
	    		}
	    		if(user.getPhone() != null)
	    		{
		    		if(!isValidPhoneNumber(user.getPhone()))
		    		{
		    			error += "Invalid phone number. The phone number should consist of 10 digits.\n";
		    		}
	    		}
	    		
	    		if(error.equals(""))
	    		{
		    		userRepository.insert(user);
		    		StylistAvailability sa = new StylistAvailability();
		    		sa.setStylistID(user.getId());
		    		sar.insert(sa);
		    		//TODO add link to avail?
		    		Resource<User> resource = ResourceHandler.createResource(user);
		    		return new ResponseEntity<Resource<User>>(resource, HttpStatus.CREATED);
	    		}
	    		else
	    		{
	    			throw new GenericUserException(error + "Exception generated in call to addUser().", HttpStatus.BAD_REQUEST);
	    		}
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
    											  HttpServletRequest request) throws GenericUserException
    {
    	L.info("GET called at path /{userID}.");
    	try
    	{
			this.setRequestControllerState(request);
		} 
    	catch (Exception e) 
    	{
			throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    	
    	User user;
    	
    	try
    	{
    		switch(authType)
    		{
	    		case CLIENT: user = userRepository.findByToken(authToken);
			    			 if(!user.getId().equals(userID))
			    			 {
			    				 throw new GenericUserException("A client only has access to their own user from the repository. "
			    						+ "Exception generated in call to getUser().", HttpStatus.FORBIDDEN);
			    			 }
			    			 break;
	    		case STYLIST: user = userRepository.findById(userID);
		    				  if(!user.isActive())
		    				  {
		    					  user = null;
		    				  }
		    				  break;
	    		case ADMIN: user = userRepository.findById(userID);
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
    												 HttpServletRequest request,
    									   			 @RequestBody User user) throws GenericUserException
    {
    	L.info("PUT called at path /{userID}.");
    	try
    	{
			this.setRequestControllerState(request);
		} 
    	catch (Exception e) 
    	{
			throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    	
    	User curUser;
    	String error = "";
    	
    	try
    	{
    		switch(authType)
    		{
	    		case CLIENT: curUser = userRepository.findByToken(authToken);
		    				 if(!curUser.getId().equals(userID))
			    			 {
		    					 throw new GenericUserException("A client only has access to their own user from the repository. "
			    						+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
			    			 }
		    				 // update client specific editable fields
		    				 // currently none
		    				 break;
	    		case STYLIST: curUser = userRepository.findByToken(authToken);
		    				  if(!curUser.getId().equals(userID))
			    			  {
		    					  throw new GenericUserException("A stylist only has access to their own user from the repository. "
			    						+ "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
			    			  }
		    				  // update stylist specific editable fields
		    				  error += updateStylist(user, curUser);
		    				  break;
	    		case ADMIN: curUser = userRepository.findById(userID);
						
							if(curUser.getGroup() != UserType.CLIENT)
							{
								// currently, admin and stylist editable fields are the same
								error += updateStylist(user, curUser);
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
	    			error += "Invalid phone number. The phone number should consist of 10 digits.\n";
	    		}
    		}
    		
    		if(error.equals(""))
    		{
	    		userRepository.save(curUser);
	        	return new ResponseEntity<Resource<User>>(ResourceHandler.createResource(user), HttpStatus.ACCEPTED);
    		}
    		else
    		{
    			throw new GenericUserException(error + "Exception generated in call to updateUser().", HttpStatus.BAD_REQUEST);
    		}
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
    
    private String updateStylist(User user, User curUser) throws GenericUserException
    {
    	String error = "";
    	if(user.getFirstName() != null)
		{
			if(isValidName(user.getFirstName()))
			{
				curUser.setFirstName(user.getFirstName());
			}
			else
			{
				error += "Invalid first name. Names should consist of only unicode letters.\n";
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
				error += "Invalid last name. Names should consist of only unicode letters.\n";
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
				error += "Invalid email.\n";
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
				error += "Invalid avatarURL. The avatarURL should be an image URL ending in .png, .jpg, or .gif.\n";
			}
		}
		//TODO change password
		
		return error;
    }
    
    private boolean isValidGroup(UserType group)
    {
    	if(group == UserType.STYLIST || group == UserType.ADMIN)
    	{
    		return true;
    	}
    	
    	return false;
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
    
    private boolean isValidPassword(String password)
    {
    	//TODO
    	return true;
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
    	L.error("Exception thrown: ", e);
    	return new ResponseEntity<String>(reason, e.getStatus());
    }
    
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
