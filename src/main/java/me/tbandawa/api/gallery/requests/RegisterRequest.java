package me.tbandawa.api.gallery.requests;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {
	
	@NotBlank
	@Size(max = 50)
	private String firstname;
	  
	@NotBlank
	@Size(max = 50)
	private String lastname;
	
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	  
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	private Set<String> role;
}
