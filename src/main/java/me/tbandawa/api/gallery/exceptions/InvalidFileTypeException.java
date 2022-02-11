package me.tbandawa.api.gallery.exceptions;

public class InvalidFileTypeException extends RuntimeException {
	
	public InvalidFileTypeException(String message, Throwable throwable) {
		super(message, throwable);
	}	

}
