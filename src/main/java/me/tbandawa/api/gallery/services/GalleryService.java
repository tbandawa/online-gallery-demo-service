package me.tbandawa.api.gallery.services;

import java.util.List;
import java.util.Optional;

import me.tbandawa.api.gallery.entities.Gallery;

public interface GalleryService {
	
	Gallery saveGallery(Gallery gallery);
	
	List<Gallery> getAllGallery();
	
	Optional<Gallery> getGallery(long id);
	
	void deleteGallery(long id);

}
