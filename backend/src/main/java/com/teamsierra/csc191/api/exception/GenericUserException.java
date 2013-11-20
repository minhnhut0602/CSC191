package com.teamsierra.csc191.api.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.teamsierra.csc191.api.controller.UserController;

public class GenericUserException extends GenericException
{
	private static final Log L = LogFactory.getLog(UserController.class);
	
	public GenericUserException(String message, HttpStatus status)
	{
		super(message, status, L);
	}
}