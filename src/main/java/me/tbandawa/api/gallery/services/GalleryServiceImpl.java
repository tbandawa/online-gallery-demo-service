package me.tbandawa.api.gallery.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.tbandawa.api.gallery.daos.GalleryDao;
import me.tbandawa.api.gallery.entities.Gallery;
import me.tbandawa.api.gallery.entities.Images;
import me.tbandawa.api.gallery.entities.PagedGallery;
import me.tbandawa.api.gallery.exceptions.ResourceNotFoundException;
import me.tbandawa.api.gallery.requests.GalleryRequest;
import me.tbandawa.api.gallery.responses.GalleryResponse;
import me.tbandawa.api.gallery.responses.PagedGalleryResponse;

@Service
public class GalleryServiceImpl implements GalleryService {
	
	@Autowired
	private GalleryDao galleryDao;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private GalleryMapper galleryMapper;

	@Override
	public GalleryResponse saveGallery(GalleryRequest galleryRequest, MultipartFile[] gallery_images) {
		Gallery gallery = this.galleryDao.save(galleryMapper.mapToGallery(galleryRequest));
		
		GalleryResponse galleryResponse = galleryMapper.mapToGalleryResponse(gallery);
		if (gallery_images.length > 0 && !gallery_images[0].isEmpty()) {
			galleryResponse.setImages(imageService.saveImages(galleryResponse.getId(), gallery_images));
		} else {
			galleryResponse.setImages(new ArrayList<Images>());
		}
		
		return galleryResponse;
	}

	@Override
	public PagedGalleryResponse getGalleries(int pageNumber) {
		
		PagedGalleryResponse pagedResults = new PagedGalleryResponse();
		PagedGallery pagedGallery = galleryDao.getGalleries(pageNumber);
		pagedResults.setCount(pagedGallery.getCount());
		pagedResults.setPerPage(pagedGallery.getPerPage());
		pagedResults.setCurrentPage(pagedGallery.getCurrentPage());
		pagedResults.setNextPage(pagedGallery.getNextPage());
		
		List<Gallery> galleries = (List<Gallery>)pagedGallery.getGallaries();
		
		List<GalleryResponse> alleryResponses = galleryMapper.mapToGalleryResponse(galleries);
		
		alleryResponses
			.stream()
			.peek(gallery -> gallery.setImages(imageService.getImages(gallery.getId())))
			.collect(Collectors.toList());
		
		pagedResults.setGalleries(alleryResponses);
		
		return pagedResults;
	}
	
	@Override
	public List<GalleryResponse> searchGallery(String query) {
		return galleryMapper.mapToGalleryResponse(galleryDao.searchGallery(query))
					.stream()
					.peek(gallery -> gallery.setImages(imageService.getImages(gallery.getId())))
					.collect(Collectors.toList());
	}

	@Override
	public GalleryResponse getGallery(long id) {
		Gallery gallery = galleryDao.get(id)
				.orElseThrow(() -> new ResourceNotFoundException("Gallery with id: " + id + " not found"));
		GalleryResponse galleryResponse = galleryMapper.mapToGalleryResponse(gallery);
		galleryResponse.setImages(imageService.getImages(gallery.getId()));
		return galleryResponse;
	}

	@Override
	public void deleteGallery(long id) {
		imageService.deleteImages(id);
		this.galleryDao.delete(id);
	}
}
