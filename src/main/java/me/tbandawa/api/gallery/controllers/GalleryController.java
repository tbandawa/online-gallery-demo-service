package me.tbandawa.api.gallery.controllers;

import java.util.List;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.tbandawa.api.gallery.requests.GalleryRequest;
import me.tbandawa.api.gallery.responses.GalleryResponse;
import me.tbandawa.api.gallery.responses.PagedGalleryResponse;
import me.tbandawa.api.gallery.services.GalleryService;
import me.tbandawa.api.gallery.services.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "gallery", description = "upload, view, delete APIs")
@CrossOrigin(origins = "http://localhost:4200")
public class GalleryController {
	
	@Autowired
	private GalleryService galleryService;
	
	/**
	 * Create new gallery
	 */
	@Operation(summary = "create new gallery", description = "send request-body object with multipart", tags = { "gallery" })
	@PostMapping(value = "/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<GalleryResponse> createGallery(
			Authentication authentication,
			@Valid GalleryRequest gallery,
			@RequestPart(value = "gallery_images", required = false) MultipartFile[] gallery_images) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		gallery.setUserId(userDetails.getId());
		return ResponseEntity.ok().body(galleryService.saveGallery(gallery, gallery_images));
	}
	
	/**
	 * Get all galleries
	 */
	@Operation(summary = "get paged galleries", description = "retrieves a list of paged galleries", tags = { "gallery" })
	@GetMapping("/galleries/{pageNumber}")
	public ResponseEntity<PagedGalleryResponse> getGalleries(@PathVariable(value = "pageNumber") int pageNumber) {
		return ResponseEntity.ok().body(galleryService.getGalleries(pageNumber));
	}
	
	/**
	 * Search galleries
	 */
	@Operation(summary = "searches galleries", description = "retrieves a list of searched galleries", tags = { "gallery" })
	@GetMapping("/search/{query}")
	public ResponseEntity<List<GalleryResponse>> searchGalleries(@PathVariable(value = "query") String query) {
		return ResponseEntity.ok().body(galleryService.searchGallery(query));
	}
	
	/**
	 * Get a single gallery
	 */
	@Operation(summary = "get a single gallery", description = "get gallery by <b>galleryId</b>", tags = { "gallery" })
	@GetMapping("/gallery/{id}")
	public ResponseEntity<GalleryResponse> getGallery(@PathVariable(value = "id") Long galleryId) {
		return ResponseEntity.ok().body(galleryService.getGallery(galleryId));
	}
	
	/**
	 * Delete a single gallery
	 */
	@Operation(summary = "delete a single gallery", description = "delete gallery by <b>galleryId</b>", tags = { "gallery" })
	@DeleteMapping("/gallery/{id}")
	public ResponseEntity<?> deleteGallery(@PathVariable(value = "id") Long galleryId) {
		galleryService.deleteGallery(galleryId);
	    return ResponseEntity.ok().build();
	}
}
