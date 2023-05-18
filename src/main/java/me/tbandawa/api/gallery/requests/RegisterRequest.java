package me.tbandawa.api.gallery.requests;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {
	
	private Long userId;
	
	@NotBlank(message = "First name can not be empty")
	@Size(max = 50, message = "First name can not be longer than 50 characters")
	private String firstname;
	  
	@NotBlank(message = "Last name can not be empty")
	@Size(max = 50, message = "Last name can not be longer than 50 characters")
	private String lastname;
	
	@NotBlank(message = "Username can not be empty")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
	private String username;

	@NotBlank(message = "Email can not be empty")
	@Email(message = "Invalid email")
	private String email;
	  
	@NotBlank(message = "Password can not be empty")
	@Size(min = 6, max = 40, message = "Password can not be empty")
	private String password;

	private Set<String> role;
}
