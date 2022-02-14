package me.tbandawa.api.gallery.exceptions;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import me.tbandawa.api.gallery.models.ErrorResponse;

@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE) 
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
	 * Handle resource not found exception
	 * @param ex the exception
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
    		ResourceNotFoundException ex, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withError(HttpStatus.NOT_FOUND.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
	/**
	 * Handle invalid file type exception
	 * @param ex the exception
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
    @ExceptionHandler(InvalidFileTypeException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidFileType(
    		InvalidFileTypeException ex, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
	/**
	 * Handle internal system errors
	 * @param ex the exception
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	protected ResponseEntity<ErrorResponse>
	handleCustomException(RuntimeException ex) {
		ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
				.withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.withError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
				.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Handle method not supported exception
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
    @Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
    	ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.METHOD_NOT_ALLOWED.value())
                .withError(status.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	/**
	 * Handle entity field missing or invalid exception
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		// Get error messages
		List<String> messages = ex.getBindingResult().getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
    	ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
    			.withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(status.getReasonPhrase())
                .withMessages(messages)
                .build();
    	return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}