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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.security.core.AuthenticationException;

import me.tbandawa.api.gallery.responses.ErrorResponse;

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
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex){
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
    		InvalidFileTypeException ex){
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
	/**
	 * Handle unfinished operation exception
	 * @param ex the exception
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
    @ExceptionHandler(NotProcessedException.class)
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleNotProcessed(NotProcessedException ex){
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .withError(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

	/**
	 * Handle authentication exception
	 * @param ex the exception
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthrntication(AuthenticationException ex){
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.UNAUTHORIZED.value())
                .withError(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    /**
	 * Handle conflicting operation exception
	 * @param ex the exception
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflicting(ResourceConflictException ex){
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.CONFLICT.value())
                .withError(HttpStatus.CONFLICT.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
    }
    
	/**
	 * Handle internal system errors
	 * @param ex the exception
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
	@ExceptionHandler({
		RuntimeException.class,
		ServiceException.class,
		FileStorageException.class
	})
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleCustomException(RuntimeException ex) {
		ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
				.withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.withError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.withMessages((List<String>) Arrays.asList(new String[] {ex.getLocalizedMessage()}))
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
                .withStatus(status.value())
                .withError(status.getReasonPhrase())
                .withMessages((List<String>) Arrays.asList(new String[] {ex.getMessage()}))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}
    
	/**
	 * Handle field request violations
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} wrapping {@code ErrorResponse}
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> messages = ex.getBindingResult().getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
    	ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
    			.withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(status.value())
                .withError(status.getReasonPhrase())
                .withMessages(messages)
                .build();
    	return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
		List<String> messages = ex.getBindingResult().getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
    	ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
    			.withTimeStamp(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(status.value())
                .withError(status.getReasonPhrase())
                .withMessages(messages)
                .build();
    	return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}