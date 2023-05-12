package me.tbandawa.api.gallery.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.tbandawa.api.gallery.entities.Images;

@Data
@AllArgsConstructor
public class UserResponse {
	
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private List<GalleryResponse> gallery;
	private Images profilePhoto;
}
