package me.tbandawa.api.gallery.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
	
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
}
