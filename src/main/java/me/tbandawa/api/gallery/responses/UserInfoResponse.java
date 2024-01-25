package me.tbandawa.api.gallery.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.tbandawa.api.gallery.entities.Images;

@Data
@AllArgsConstructor
public class UserInfoResponse {
	private String firstname;
	private String lastname;
	private Images profilePhoto;
}
