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
import me.tbandawa.api.gallery.responses.UserInfoResponse;
import me.tbandawa.api.gallery.responses.UserResponse;

@Service
public class GalleryServiceImpl implements GalleryService {
	
	@Autowired
	private GalleryDao galleryDao;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private GalleryMapper galleryMapper;
	
	@Autowired
	private UserService userService;

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
		
		List<GalleryResponse> galleryResponses = galleryMapper.mapToGalleryResponse(galleries);
		
		galleryResponses
			.stream()
			.peek(gallery -> {
				UserResponse userResponse = userService.getUserProfile(gallery.getUserId());
				Images images = new Images();
				images.setThumbnail(userResponse.getProfilePhoto().getThumbnail());
				images.setImage(userResponse.getProfilePhoto().getImage());
				UserInfoResponse userInfo = new UserInfoResponse(userResponse.getFirstname(), userResponse.getLastname(), images);
				gallery.setUser(userInfo);
				gallery.setImages(imageService.getImages(gallery.getId()));
			})
			.collect(Collectors.toList());
		
		pagedResults.setGalleries(galleryResponses);
		
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
		
		UserResponse userResponse = userService.getUserProfile(gallery.getUserid());
		Images images = new Images();
		images.setThumbnail(userResponse.getProfilePhoto().getThumbnail());
		images.setImage(userResponse.getProfilePhoto().getImage());
		UserInfoResponse userInfo = new UserInfoResponse(userResponse.getFirstname(), userResponse.getLastname(), images);
		
		GalleryResponse galleryResponse = galleryMapper.mapToGalleryResponse(gallery);
		galleryResponse.setImages(imageService.getImages(gallery.getId()));
		galleryResponse.setUser(userInfo);
		return galleryResponse;
	}

	@Override
	public void deleteGallery(long id) {
		imageService.deleteImages(id);
		this.galleryDao.delete(id);
	}
}
