package me.tbandawa.api.gallery.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.tbandawa.api.gallery.daos.GalleryDao;
import me.tbandawa.api.gallery.entities.Gallery;
import me.tbandawa.api.gallery.exceptions.ResourceNotFoundException;
import me.tbandawa.api.gallery.requests.GalleryRequest;
import me.tbandawa.api.gallery.responses.GalleryResponse;

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
	public List<GalleryResponse> getAllGallery() {
		return galleryMapper.mapToGalleryResponse(galleryDao.getAll());
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
