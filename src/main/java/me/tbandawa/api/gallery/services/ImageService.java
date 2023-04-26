package me.tbandawa.api.gallery.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import me.tbandawa.api.gallery.models.Images;

public interface ImageService {
	
	List<Images> saveImages(Long galleryId, MultipartFile[] images);
	
	//List<Images> getImages(Long galleryId);
	
	void deleteImages(Long galleryId);

}
