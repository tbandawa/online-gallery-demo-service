package me.tbandawa.api.gallery.exceptions;

@SuppressWarnings("serial")
public class ResourceConflictException extends RuntimeException {

	public ResourceConflictException(String message) {
		super(message);
	}
}
