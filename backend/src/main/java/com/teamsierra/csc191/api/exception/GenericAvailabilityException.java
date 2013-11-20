package com.teamsierra.csc191.api.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.teamsierra.csc191.api.controller.AvailabilityController;

public class GenericAvailabilityException extends GenericException
{
	private static final Log L = LogFactory.getLog(AvailabilityController.class);
	
	public GenericAvailabilityException(String message, HttpStatus status)
	{
		super(message, status, L);
	}
}
