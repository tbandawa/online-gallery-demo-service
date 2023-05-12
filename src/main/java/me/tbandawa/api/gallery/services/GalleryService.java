package me.tbandawa.api.gallery.services;

import java.util.List;

import me.tbandawa.api.gallery.requests.GalleryRequest;
import me.tbandawa.api.gallery.responses.GalleryResponse;

public interface GalleryService {
	GalleryResponse saveGallery(GalleryRequest galleryRequest);	
	List<GalleryResponse> getAllGallery();	
	GalleryResponse getGallery(long id);	
	void deleteGallery(long id);
}
