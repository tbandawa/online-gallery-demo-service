package me.tbandawa.api.gallery.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException {
	
	public FileStorageException(String message) {
		super(message);
	}	
}
