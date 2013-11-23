package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.exception.GenericUserException;
import com.teamsierra.csc191.api.model.GenericModel.UserType;
import com.teamsierra.csc191.api.model.StylistAvailability;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
import com.teamsierra.csc191.api.resources.ResourceHandler;
import com.teamsierra.csc191.api.util.Availability;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * User: scott
 * Date: 9/14/13
 * Time: 10:21 AM
 *
 * TODO add hairColor, hairLenght to editUser
 */

@Controller
@RequestMapping("/users")
public class UserController extends GenericController
{
    private static final Log L = LogFactory.getLog(UserController.class);

    private UserRepository userRepository;
    private StylistAvailabilityRepository sar;

    @Autowired
    public UserController(UserRepository userRepo, StylistAvailabilityRepository stylistAvailRepo)
    {
    	userRepository = userRepo;
    	sar = stylistAvailRepo;
    }
    
    /**
     * A method to retrieve all of the users that the current user has access to.
     * 
     * Usage: GET call to /users.
     * 	Client - returns their own user object from the db.
     * 	Stylist - returns all active users in the db.
     * 	Admin - returns all users in the db.
     * 
     * Input:
     * 	-Headers: authType, authToken
     * 
     * Return: List<Resource<User>>. Will throw an exception if this list is empty
     * 	or null.
     * 	
     * 	format - Json format of the resource with a link to each individual user and,
     * 		in the case of a stylist or admin, a link to their availability as well.
     * 		Fields may have a value of null if they are empty.
     * 
     * 	the following is an example of what would be returned if the user was an Admin
     * 	and there were currently two users in the database, one admin and one client:
     * { 
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			},
     * 			{
     * 				"rel": "availability"
     * 				"href": ".../availability/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "ADMIN",
     * 		"firstName": "Kyle",
     * 		"lastName": "Matz",
     * 		"email": "kmatz4b@gmail.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9162223333",
     * 		"active": true
     * 	},
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "CLIENT",
     * 		"firstName": "John",
     * 		"lastName": "Smith",
     * 		"email": "iFailAtCreativity@yahoo.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9164445555",
     * 		"active": false
     *	}
     * }
     * 
     * @param request requires headers "authType" and "authToken"
     * @return 
     * @throws GenericUserException see message for why exception was thrown
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Resource<User>>> getUsers(HttpServletRequest request) throws GenericUserException
    {
    	try
    	{
    		this.setRequestControllerState(request);
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	
    	List<User> users = null;
    	
    	switch(authType)
		{
    		case CLIENT: users = new ArrayList<User>();
						 User user = userRepository.findByToken(authToken);
						 if(user != null)
						 {
							users.add(user);
						 }
		    			
		    			 break;
    		case STYLIST: users = userRepository.findAllActive();
    					  break;
    		case ADMIN: users = userRepository.findAll();
    					break;
			default: throw new GenericUserException("Unknown authType header. "
							+ "Exception generated in call to getUsers().",
							HttpStatus.BAD_REQUEST);
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
    		throw new GenericUserException("No user(s) found in the database. "
    				+ "Exception generated in call to getUsers()",
    				HttpStatus.NOT_FOUND);
    	}
    }
    
    /**
     * Adds a new user to the database after validating the fields. Only Admins
     * can create new users and these users must be either Stylists or Admins.
     * 
     * NOTE: "active" is assumed to be false if not included.
     * 
     * Required fields: type, firstName, lastName, email, password
     * Optional fields: phone, avatarURL, authId, token, active
     * Null fields: id (should be null, throws an exception if it is not)
     * 
     * Not checked Fields: authId, token (will be set if included, no 
     * 					   validation done)
     * 
     * Usage: POST call to /users.
     * 	Client - will throw an exception
     * 	Stylist - will throw an exception
     * 	Admin - no other case where an exception will be thrown.
     * 
     * Input:
     * 	-Headers: authType
     * 	-RequestBody: a Json formatted User model with the required fields for
     * 		a user filled out.
     * 		example:
     * 		{
     * 			"type": "ADMIN",
     * 			"firstName": "name",
     * 			"lastName": "lastname",
     * 			"email": "something@somethingElse.com",
     * 			"password": "password"
     * 		}
     * 
     * Return: Resource<User>. Will throw an exception if the specified
     * 	fields for the user fail to pass validation.
     * 	
     * 	format - Json format of the resource with a link to the user and
     * 		a link to their availability as well. Fields may have a value
     * 		of null if they are empty.
     * 
     * 	the following is an example of what would be returned:
     * 	stylist/admin:
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			},
     * 			{
     * 				"rel": "availability"
     * 				"href": ".../availability/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "ADMIN",
     * 		"firstName": "Kyle",
     * 		"lastName": "Matz",
     * 		"email": "kmatz4b@gmail.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9162223333",
     * 		"active": true
     * 	}
     * 	
     * 	client:
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "CLIENT",
     * 		"firstName": "John",
     * 		"lastName": "Smith",
     * 		"email": "iFailAtCreativity@yahoo.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9164445555",
     * 		"active": false
     *	}
     * 
     * @param request
     * @param user
     * @return
     * @throws GenericUserException
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<User>> addUser(HttpServletRequest request,
                                        		  @RequestBody User user) throws GenericUserException 
    {
    	try
    	{
    		this.setRequestControllerState(request);
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	
    	if(authType == UserType.ADMIN)
    	{
    		//TODO password stuff
    		// required fields
    		String error = "";
    		if(!isValidType(user.getType()))
    		{
    			error  += "Invalid user type. Type should be either stylist or admin.\n";
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
    		
    		//fields that should be null
    		if(user.getId() != null)
    		{
    			error += "User ID should be null when creating a new user. To update an"
    					+ "existing user use \".../users/{userID} PUT\"";
    		}
    		
    		// fields not checked: authId, token, active
    		
    		if(error.equals(""))
    		{
	    		userRepository.insert(user);
	    		StylistAvailability sa = new StylistAvailability();
	    		sa.setStylistID(user.getId());
	    		sa.setAvailability(new Availability());
	    		sar.insert(sa);
	    		
	    		Resource<User> resource = ResourceHandler.createResource(user);
	    		return new ResponseEntity<Resource<User>>(resource, HttpStatus.CREATED);
    		}
    		else
    		{
    			throw new GenericUserException(error + "Exception generated in call to addUser().",
    					HttpStatus.BAD_REQUEST);
    		}
    	}
    	else
    	{
    		throw new GenericUserException("Admin credentials are required to create a new user. "
    				+ "Exception generated in call to addUser().",
    				HttpStatus.FORBIDDEN);
    	}
    }
    
    /**
     * Retrieves a specific user from the database by their id.
     * 
     * Usage: GET call to /users/{userID}.
     * 	Client - will throw an exception if trying to access a different user
     * 	Stylist - will throw an exception if the user's active field is false 
     * 	Admin - no other case where an exception will be thrown.
     * 
     * Input:
     * 	-PathVariable: userID
     * 	-Headers: authType, authToken
     * 
     * Return: Resource<User>. Will throw an exception if the specified
     * 	user does not exist in the database.
     * 	
     * 	format - Json format of the resource with a link to the user and,
     * 		in the case of a stylist or admin, a link to their availability as well.
     * 		Fields may have a value of null if they are empty.
     * 
     * 	the following is an example of what would be returned:
     * 	stylist/admin:
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			},
     * 			{
     * 				"rel": "availability"
     * 				"href": ".../availability/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "ADMIN",
     * 		"firstName": "Kyle",
     * 		"lastName": "Matz",
     * 		"email": "kmatz4b@gmail.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9162223333",
     * 		"active": true
     * 	}
     * 
     * 	client:
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "CLIENT",
     * 		"firstName": "John",
     * 		"lastName": "Smith",
     * 		"email": "iFailAtCreativity@yahoo.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9164445555",
     * 		"active": false
     *	}
     * 
     * @param userID
     * @param request
     * @return
     * @throws GenericUserException
     */
    @RequestMapping(value = "/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<User>> getUser(@PathVariable String userID,
    											  HttpServletRequest request) throws GenericUserException
    {
    	try
    	{
    		this.setRequestControllerState(request);
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	
    	User user;
    	
		switch(authType)
		{
            case CLIENT: user = userRepository.findById(userID);
                        if(user == null)
                        {
                            throw new GenericUserException("Unable to find the requested user in the database.",
                                    HttpStatus.NOT_FOUND);
                        }

                        User curUser = userRepository.findByToken(authToken);

                        if (curUser != null && user.getType() == UserType.CLIENT && !userID.equals(curUser.getId()))
                        {
                            user = null;
                        }
                        if(user != null && !user.isActive())
                        {
                            user = null;
                        }
                        break;
    		case STYLIST: user = userRepository.findById(userID);
			    		  if(user == null)
						  {
			    			  throw new GenericUserException("Unable to find the requested user in the database.",
									 HttpStatus.NOT_FOUND);
						  }
			    		  
	    				  if(!user.isActive())
	    				  {
	    					  user = null;
	    				  }
	    				  break;
    		case ADMIN: user = userRepository.findById(userID);
    					break;
			default: throw new GenericUserException("Unknown authType header. "
							+ "Exception generated in call to getUser().",
							HttpStatus.BAD_REQUEST);
		}
    	
		if(user != null)
		{
			return new ResponseEntity<Resource<User>>(ResourceHandler.createResource(user), HttpStatus.OK);
		}
		else
		{
			throw new GenericUserException("User not found in the database. "
					+ "Exception generated in call to getUser().",
					HttpStatus.NOT_FOUND);
		}
    }
    
    /**
     * Updates a user based on the fields provided. Will only check and validate
     * fields that the specific user is allowed to edit, other fields will be
     * ignored (with no notification).
     * 
     * Modifiable fields by user type:
     * 	CLIENT: phone
     * 	STYLIST: avatarURL, email, firstName, lastName, phone
     *	ADMIN: avatarURL, email, firstName, lastName, phone
     *
     * including any additional fields will not throw an error, but will have
     * no affect.
     * 
     * NOTE: The "active" field should always be included as booleans are assumed
     * 	to be false if not included.
     * 
     * Usage: PUT call to /users/{userID}.
     * 	Client - allowed edit their own user, will throw an exception otherwise
     * 	Stylist - allowed to edit their own user, will thrown and exception otherwise
     * 	Admin - can edit any existing user
     * 
     * Input:
     * 	-PathVariable: userID
     * 	-Headers: authType, authToken
     * 	-RequestBody: a Json formatted User model which only requires the fields that
     * 		are being updated to be present.
     * 
     * 		example of changing first name and phone number:
     * 		{
     * 			"firstName": "name",
     * 			"phone": "1234567890",
     * 			"active": true
     * 		}
     * 
     * Return: Resource<User>. Will throw an exception if the specified
     * 	fields for the user fail to pass validation or if the user does not exist.
     * 	
     * 	format - Json format of the resource with a link to each individual user and,
     * 		in the case of a stylist or admin, a link to their availability as well.
     * 		Fields may have a value of null if they are empty.
     * 
     * 	the following is an example of what would be returned:
     * 	stylist/admin:
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			},
     * 			{
     * 				"rel": "availability"
     * 				"href": ".../availability/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "ADMIN",
     * 		"firstName": "Kyle",
     * 		"lastName": "Matz",
     * 		"email": "kmatz4b@gmail.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9162223333",
     * 		"active": true
     * 	}
     * 
     * 	client:
     * 	{
     * 		"links":
     * 		[
     * 			{
     * 				"rel": "self",
     * 				"href": ".../users/{userID}"
     * 			}
     * 		]
     * 		"id": "{userID}",
     * 		"type": "CLIENT",
     * 		"firstName": "John",
     * 		"lastName": "Smith",
     * 		"email": "iFailAtCreativity@yahoo.com",
     * 		"avatarURL": "somePic.gif",
     * 		"phone": "9164445555",
     * 		"active": false
     *	}
     * 
     * @param userID
     * @param request
     * @param user
     * @return
     * @throws GenericUserException
     */
    @RequestMapping(value = "/{userID}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<User>> updateUser(@PathVariable String userID,
    												 HttpServletRequest request,
    									   			 @RequestBody User user) throws GenericUserException
    {
    	try
    	{
    		this.setRequestControllerState(request);
    	}
    	catch(Exception e)
    	{
    		throw new GenericUserException(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	
    	User curUser;
    	String error = "";
    	
		switch(authType)
		{
    		case CLIENT: curUser = userRepository.findByToken(authToken);
    		
    					 if(curUser == null)
    					 {
    						 throw new GenericUserException("Unable to find your user credentials in the database.",
    								 HttpStatus.FORBIDDEN);
    					 }
    					 
	    				 if(!curUser.getId().equals(userID))
		    			 {
	    					 throw new GenericUserException("A client only has access to their own user from the repository. "
		    						+ "Exception generated in call to updateUser().",
		    						HttpStatus.BAD_REQUEST);	 
		    			 }
	    				 // update client specific editable fields
	    				 // currently none
	    				 break;
    		case STYLIST: curUser = userRepository.findByToken(authToken);
    		
			    		  if(curUser == null)
						  {
			    			  throw new GenericUserException("Unable to find your user credentials in the database.",
									 HttpStatus.FORBIDDEN);
						  }
			    		  
	    				  if(!curUser.getId().equals(userID))
		    			  {
	    					  throw new GenericUserException("A stylist only has access to their own user from the repository. "
		    						+ "Exception generated in call to updateUser().",
		    						HttpStatus.BAD_REQUEST);
		    			  }
	    				  // update stylist specific editable fields
	    				  error += updateStylist(user, curUser);
	    				  break;
    		case ADMIN: curUser = userRepository.findById(userID);
						
			    		if(curUser == null)
						{
							throw new GenericUserException("Unable to find requested user in the database.",
									 HttpStatus.NOT_FOUND);
						}
						if(curUser.getType() != UserType.CLIENT)
						{
							// currently, admin and stylist editable fields are the same
							error += updateStylist(user, curUser);
						}
						
						curUser.setActive(user.isActive());
	    				break;
			default: throw new GenericUserException("Unknown authType header. "
							+ "Exception generated in call to updateUser().",
							HttpStatus.BAD_REQUEST);
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
			throw new GenericUserException(error + "Exception generated in call to updateUser().",
					HttpStatus.BAD_REQUEST);
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
    
    private boolean isValidType(UserType type)
    {
    	if(type == UserType.STYLIST || type == UserType.ADMIN)
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
    
    //@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception e) throws GenericUserException
    {
		if(!(e instanceof GenericUserException))
		{
			throw new GenericUserException(e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		else
		{
			throw (GenericUserException) e;
		}
    }
}
