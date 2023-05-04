package me.tbandawa.api.gallery.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.tbandawa.api.gallery.daos.GalleryDao;
import me.tbandawa.api.gallery.entities.Gallery;

@Service
public class GalleryServiceImpl implements GalleryService {
	
	@Autowired
	private GalleryDao galleryDao;

	@Override
	public Gallery saveGallery(Gallery gallery) {
		return this.galleryDao.save(gallery);
	}

	@Override
	public List<Gallery> getAllGallery() {
		return this.galleryDao.getAll();
	}

	@Override
	public Optional<Gallery> getGallery(long id) {
		return this.galleryDao.get(id);
	}

	@Override
	public void deleteGallery(long id) {
		this.galleryDao.delete(id);
	}

}
