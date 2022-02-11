package me.tbandawa.api.gallery.daos;

public interface GalleryDao {
	
	Optional<Gallery> get(long id);

    List<Gallery> getAll();
    
    Article save(Gallery gallery);
    
    void delete(long id);

}
