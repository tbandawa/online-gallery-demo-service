package me.tbandawa.api.gallery.requests;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstanme) {
		this.firstname = firstanme;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}
}
