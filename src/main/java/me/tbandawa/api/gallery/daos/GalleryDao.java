package me.tbandawa.api.gallery.daos;

import java.util.List;
import java.util.Optional;

import me.tbandawa.api.gallery.entities.Gallery;

public interface GalleryDao {
	Optional<Gallery> get(long id);
    List<Gallery> getAll();
    List<Gallery> searchGallery(String query);
    Gallery save(Gallery gallery);
    void delete(long id);
}
