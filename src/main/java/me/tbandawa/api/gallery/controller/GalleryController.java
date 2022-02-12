package me.tbandawa.api.gallery.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import me.tbandawa.api.gallery.exceptions.ResourceNotFoundException;
import me.tbandawa.api.gallery.models.Gallery;
import me.tbandawa.api.gallery.services.GalleryService;
import me.tbandawa.api.gallery.services.ImageService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class GalleryController {
	
	@Autowired
	private GalleryService galleryService;
	
	@Autowired
	private ImageService imageService;
	
	//create article
	@PostMapping(value = "/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Gallery createArticle(
			@Valid Gallery gallery,
			@RequestPart(value = "gallery_images", required = false) MultipartFile[] gallery_images) {
		Gallery savedGallery = galleryService.saveGallery(gallery);
		
		if (gallery_images.length > 0 && !gallery_images[0].isEmpty()) {
			savedGallery.setImages(imageService.saveImages(savedGallery.getId(), gallery_images));
		} else {
			savedGallery.setImages(new ArrayList<String>());
		}
		return savedGallery;
	}
	
	//get all articles
	@GetMapping("/gallery")
	public List<Gallery> getGallery() {
		return galleryService.getAllGallery().stream()
				.map(gallery -> {
					gallery.setImages(imageService.getImages(gallery.getId()));
					return gallery;
				})
				.collect(Collectors.toList());
	}
	
	//get single articles
	@GetMapping("/gallery/{id}")
	public Gallery getNews(@PathVariable(value = "id") Long galleryId) {
		Gallery gallery = galleryService.getGallery(galleryId)
				.orElseThrow(() -> new ResourceNotFoundException("Gallery with id: " + galleryId + " not found"));
		gallery.setImages(imageService.getImages(gallery.getId()));
		return gallery;
	}
	
	//delete article
	@DeleteMapping("/gallery/{id}")
	public ResponseEntity<?> deleteGallery(@PathVariable(value = "id") Long galleryId) {
	    Gallery gallery = galleryService.getGallery(galleryId)
	    		.orElseThrow(() -> new ResourceNotFoundException("Gallery with id: " + galleryId + " not found"));
	    imageService.deleteImages(galleryId);
	    galleryService.deleteGallery(gallery.getId());
	    return ResponseEntity.ok().build();
	}

}
