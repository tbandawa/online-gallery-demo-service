package me.tbandawa.api.gallery.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	
	List<String> saveImages(Long galleryId, MultipartFile[] images);
	
	List<String> getImages(Long galleryId);
	
	void deleteImages(Long galleryId);

}
