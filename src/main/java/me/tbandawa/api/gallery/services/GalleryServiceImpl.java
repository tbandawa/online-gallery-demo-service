package me.tbandawa.api.gallery.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.tbandawa.api.gallery.daos.GalleryDao;
import me.tbandawa.api.gallery.entities.Gallery;
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
	private GalleryMapper galleryMapper;

	@Override
	public GalleryResponse saveGallery(GalleryRequest galleryRequest) {
		Gallery gallery = this.galleryDao.save(galleryMapper.mapToGallery(galleryRequest));
		return galleryMapper.mapToGalleryResponse(gallery);
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
		
		pagedResults.setGallaries(alleryResponses);
		
		return pagedResults;
	}
	
	@Override
	public List<GalleryResponse> searchGallery(String query) {
		return galleryMapper.mapToGalleryResponse(galleryDao.searchGallery(query));
	}

	@Override
	public GalleryResponse getGallery(long id) {
		Gallery gallery = galleryDao.get(id)
				.orElseThrow(() -> new ResourceNotFoundException("Gallery with id: " + id + " not found"));
		return galleryMapper.mapToGalleryResponse(gallery);
	}

	@Override
	public void deleteGallery(long id) {
		this.galleryDao.delete(id);
	}
}
