package me.tbandawa.api.gallery.responses;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.tbandawa.api.gallery.entities.Images;

@Data
@AllArgsConstructor
public class GalleryResponse {

    private Long id;
	
    private String title;

    private String description;
	
	private List<Images> images;
	
    private Date created;
}
