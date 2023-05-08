package me.tbandawa.api.gallery.services;

import java.util.List;

import me.tbandawa.api.gallery.entities.Gallery;
import me.tbandawa.api.gallery.requests.GalleryRequest;
import me.tbandawa.api.gallery.responses.GalleryResponse;

public interface GalleryMapper {
	Gallery mapToGallery(GalleryRequest request); 
	GalleryResponse mapToGalleryResponse(Gallery gallery); 
	List<GalleryResponse> mapToGalleryResponse(List<Gallery> galleries);
}
