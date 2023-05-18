package me.tbandawa.api.gallery.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
	
	@NotBlank(message = "Username can not be empty")
	@Size(max = 50, message = "Username can not be longer than 50 characters")
	private String username;
	  
	@NotBlank(message = "Password can not be empty")
	@Size(min = 6, max = 40, message = "Password should be between 6 and 40 characters long")
	private String password;
}
