package me.tbandawa.api.gallery.exceptions;

@SuppressWarnings("serial")
public class NotProcessedException extends RuntimeException {
	
	public NotProcessedException(String message) {
		super(message);
	}
}
