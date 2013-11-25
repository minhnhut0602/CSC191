package com.teamsierra.csc191.api.exception;

import org.apache.commons.logging.Log;
import org.springframework.http.HttpStatus;

public class GenericException extends Exception 
{
	private HttpStatus status;
	private Log log;
	
	public GenericException(String message, HttpStatus status, Log l)
	{
		super(message);
		this.status = status;
		this.log = l;
        this.log.info(this.getMessage());
        this.log.error(this.getStackTrace());
	}

	public HttpStatus getStatus() 
	{
		return status;
	}

	public void setStatus(HttpStatus status) 
	{
		this.status = status;
	}

	public Log getLog() 
	{
		return log;
	}

	public void setLog(Log l) 
	{
		this.log = l;
	}	
}
