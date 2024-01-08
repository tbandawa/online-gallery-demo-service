package me.tbandawa.api.gallery.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import me.tbandawa.api.gallery.requests.GalleryRequest;
import me.tbandawa.api.gallery.responses.GalleryResponse;
import me.tbandawa.api.gallery.responses.PagedGalleryResponse;

public interface GalleryService {
	GalleryResponse saveGallery(GalleryRequest galleryRequest, MultipartFile[] gallery_images);	
	PagedGalleryResponse getGalleries(int pageNumber);
	List<GalleryResponse> searchGallery(String query);
	GalleryResponse getGallery(long id);	
	void deleteGallery(long id);
}
