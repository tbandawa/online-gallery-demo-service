package me.tbandawa.api.gallery.exceptions;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {
	
	public ServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
