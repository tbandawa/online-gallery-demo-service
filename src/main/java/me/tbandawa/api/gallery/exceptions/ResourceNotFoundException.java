package me.tbandawa.api.gallery.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
