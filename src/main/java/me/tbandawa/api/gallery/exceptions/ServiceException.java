package me.tbandawa.api.gallery.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceException extends RuntimeException {
	
	public ServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
