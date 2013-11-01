package com.teamsierra.csc191.api.exception;

import org.springframework.http.HttpStatus;

public class GenericUserException extends Exception{
	private HttpStatus status;
	
	public GenericUserException(String message){
		super(message);
	}
	
	public GenericUserException(String message, HttpStatus status){
		super(message);
		
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
