package me.tbandawa.api.gallery.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class NotProcessedException extends RuntimeException {
	
	public NotProcessedException(String message) {
		super(message);
	}
}
