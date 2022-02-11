package me.tbandawa.api.gallery.exceptions;

public class ServiceException extends RuntimeException {
	
	public ServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
