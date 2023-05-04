package me.tbandawa.api.gallery.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	  
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;
}
