package me.tbandawa.api.gallery.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	
	List<String> saveImages(String gelleryId, MultipartFile[] images);
	
	List<String> getImages(String gelleryId);
	
	void deleteImages(String gelleryId);

}
