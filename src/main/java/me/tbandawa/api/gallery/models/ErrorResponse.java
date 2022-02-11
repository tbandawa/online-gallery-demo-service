package me.tbandawa.api.gallery.models;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
	
	private LocalDateTime timeStamp;
    private int status;
    private String error;
    private List<String> messages;
    
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	public static final class ErrorResponseBuilder {
    	
		private LocalDateTime timeStamp;
	    private int status;
	    private String error;
	    private List<String> messages;
        
        public ErrorResponseBuilder() {}
        
        public ErrorResponseBuilder errorResponseBuilder() {
        	return new ErrorResponseBuilder();
        }
        
        public ErrorResponseBuilder withTimeStamp(LocalDateTime timeStamp) {
        	this.timeStamp = timeStamp;
        	return this;
        }
        
        public ErrorResponseBuilder withStatus(int status) {
        	this.status = status;
        	return this;
        }
        
        public ErrorResponseBuilder withError(String error) {
        	this.error = error;
        	return this;
        }
        
        public ErrorResponseBuilder withMessages(List<String> messages) {
        	this.messages = messages;
        	return this;
        }
        
        public ErrorResponse build() {
        	ErrorResponse errorResponse = new ErrorResponse();
        	errorResponse.timeStamp = this.timeStamp;
        	errorResponse.status = this.status;
        	errorResponse.error = this.error;
        	errorResponse.messages = this.messages;
        	return errorResponse;
        }
        
    }

}