package me.tbandawa.api.gallery.daos;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.tbandawa.api.gallery.models.Gallery;

@Repository
@Transactional
public class GalleryDaoImpl implements GalleryDao {
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Optional<Gallery> get(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return Optional.ofNullable((Gallery)session.get(Gallery.class, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Gallery> getAll() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Gallery> galleryList = session.createQuery("FROM Gallery g ORDER BY g.id DESC").list();
		return galleryList;
	}

	@Override
	public Gallery save(Gallery gallery) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(gallery);
		return gallery;
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Gallery gallery = (Gallery)session.load(Gallery.class, id);
		if (null != gallery) {
			session.delete(gallery);
		}
	}

}
