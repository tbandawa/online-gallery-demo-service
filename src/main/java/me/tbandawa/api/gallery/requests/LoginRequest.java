package me.tbandawa.api.gallery.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
	
	  @NotBlank
	  @Size(max = 50)
	  @Email
	  private String email;
	  
	  @NotBlank
	  @Size(min = 6, max = 40)
	  private String password;
	  
	  

	public LoginRequest(@NotBlank @Size(max = 50) @Email String email,
			@NotBlank @Size(min = 6, max = 40) String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
