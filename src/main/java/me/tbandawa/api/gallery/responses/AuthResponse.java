package me.tbandawa.api.gallery.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
	
	private String token;
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private List<String> roles;
}
