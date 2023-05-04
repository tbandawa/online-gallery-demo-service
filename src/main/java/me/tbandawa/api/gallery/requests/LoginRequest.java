package me.tbandawa.api.gallery.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
	
	@NotBlank
	@Size(max = 50)
	private String username;
	  
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;
}
