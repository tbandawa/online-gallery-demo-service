package me.tbandawa.api.gallery.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.tbandawa.api.gallery.entities.Images;
import me.tbandawa.api.gallery.requests.GalleryRequest;
import me.tbandawa.api.gallery.responses.GalleryResponse;
import me.tbandawa.api.gallery.services.GalleryService;
import me.tbandawa.api.gallery.services.ImageService;
import me.tbandawa.api.gallery.services.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@Tag(name = "gallery", description = "upload, view, delete APIs")
@CrossOrigin(origins = "http://localhost:4200")
public class GalleryController {
	
	@Autowired
	private GalleryService galleryService;
	
	@Autowired
	private ImageService imageService;
	
	/**
	 * Create new gallery
	 */
	@Operation(summary = "create new gallery", description = "send request-body object with multipart", tags = { "gallery" })
	@PostMapping(value = "/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public GalleryResponse createGallery(
			Authentication authentication,
			@Valid GalleryRequest gallery,
			@RequestPart(value = "gallery_images", required = false) MultipartFile[] gallery_images) {
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		gallery.setUserId(userDetails.getId());
		
		GalleryResponse savedGallery = galleryService.saveGallery(gallery);
		if (gallery_images.length > 0 && !gallery_images[0].isEmpty()) {
			savedGallery.setImages(imageService.saveImages(savedGallery.getId(), gallery_images));
		} else {
			savedGallery.setImages(new ArrayList<Images>());
		}
		return savedGallery;
	}
	
	/**
	 * Get all galleries
	 */
	@Operation(summary = "get all galleries", description = "retrieves a list of galleries", tags = { "gallery" })
	@GetMapping("/gallery")
	public List<GalleryResponse> getGalleries() {
		return galleryService.getAllGallery().stream()
				.peek(gallery -> gallery.setImages(imageService.getImages(gallery.getId())))
				.collect(Collectors.toList());
	}
	
	/**
	 * Search galleries
	 */
	@Operation(summary = "searches galleries", description = "retrieves a list of searched galleries", tags = { "gallery" })
	@GetMapping("/search/{query}")
	public List<GalleryResponse> searchGalleries(@PathVariable(value = "query") String query) {
		return galleryService.searchGallery(query).stream()
				.peek(gallery -> gallery.setImages(imageService.getImages(gallery.getId())))
				.collect(Collectors.toList());
	}
	
	/**
	 * Get a single gallery
	 */
	@Operation(summary = "get a single gallery", description = "get gallery by <b>galleryId</b>", tags = { "gallery" })
	@GetMapping("/gallery/{id}")
	public GalleryResponse getGallery(@PathVariable(value = "id") Long galleryId) {
		GalleryResponse gallery = galleryService.getGallery(galleryId);
		gallery.setImages(imageService.getImages(gallery.getId()));
		return gallery;
	}
	
	/**
	 * Delete a single gallery
	 */
	@Operation(summary = "delete a single gallery", description = "delete gallery by <b>galleryId</b>", tags = { "gallery" })
	@DeleteMapping("/gallery/{id}")
	public ResponseEntity<?> deleteGallery(@PathVariable(value = "id") Long galleryId) {
		GalleryResponse gallery = galleryService.getGallery(galleryId);
	    imageService.deleteImages(galleryId);
	    galleryService.deleteGallery(gallery.getId());
	    return ResponseEntity.ok().build();
	}
}
