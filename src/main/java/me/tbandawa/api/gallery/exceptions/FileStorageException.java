package me.tbandawa.api.gallery.exceptions;

@SuppressWarnings("serial")
public class FileStorageException extends RuntimeException {
	
	public FileStorageException(String message, Throwable throwable) {
		super(message, throwable);
	}	
}
