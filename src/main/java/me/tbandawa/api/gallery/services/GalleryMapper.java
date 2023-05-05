package me.tbandawa.api.gallery.services;

import java.util.List;

import me.tbandawa.api.gallery.entities.Gallery;
import me.tbandawa.api.gallery.responses.GalleryResponse;

public interface GalleryMapper {
	List<GalleryResponse> mapToGalleryResponse(List<Gallery> galleries); 
}
