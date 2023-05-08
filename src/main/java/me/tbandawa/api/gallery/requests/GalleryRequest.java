package me.tbandawa.api.gallery.requests;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class GalleryRequest {
	
	@JsonIgnore
	private Long userId;
	
	@JsonIgnore
	private Long id;
	
	@NotBlank(message = "Gallery title can not be empty")
	@Length(max = 150, message = "Gallery title can not be longer than 150 characters")
    private String title;

	@NotBlank(message = "Gallery description can not be empty")
    private String description;
}
