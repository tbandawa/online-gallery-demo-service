package me.tbandawa.api.gallery.daos;

import java.util.List;
import java.util.Optional;

import me.tbandawa.api.gallery.entities.Gallery;
import me.tbandawa.api.gallery.entities.PagedGallery;

public interface GalleryDao {
	Optional<Gallery> get(long id);
    PagedGallery getGalleries(int pageNumber);
    List<Gallery> searchGallery(String query);
    Gallery save(Gallery gallery);
    void delete(long id);
}
