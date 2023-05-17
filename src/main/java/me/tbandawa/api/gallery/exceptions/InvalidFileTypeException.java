package me.tbandawa.api.gallery.exceptions;

@SuppressWarnings("serial")
public class InvalidFileTypeException extends RuntimeException {
	
	public InvalidFileTypeException(String message) {
		super(message);
	}	
}
