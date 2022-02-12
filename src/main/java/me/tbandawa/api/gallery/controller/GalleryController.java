package me.tbandawa.api.gallery.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	/*@GetMapping("/gallery/{id}")
	public Article getNews(@PathVariable(value = "id") Long articleId) {
		System.out.println(imageService.getImages("articles", String.valueOf(articleId)));
		Article article = articlesService.getArticle(articleId)
				.orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));
		article.setImages(imageService.getImages("articles", String.valueOf(article.getId())));
		return article;
	}
	
	//delete article
	@DeleteMapping("/gallery/{id}")
	public ResponseEntity<?> deleteNews(@PathVariable(value = "id") Long articleId) {
	    Article article = articlesService.getArticle(articleId)
	    		.orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));
	    imageService.deleteImages("articles", String.valueOf(articleId));
	    articlesService.deleteArticle(article.getId());
	    return ResponseEntity.ok().build();
	}*/

}
