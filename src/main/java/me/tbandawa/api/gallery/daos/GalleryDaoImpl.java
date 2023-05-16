package me.tbandawa.api.gallery.daos;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.tbandawa.api.gallery.entities.Gallery;

@Repository
public class GalleryDaoImpl implements GalleryDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly = true)
	public Optional<Gallery> get(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return Optional.ofNullable((Gallery)session.get(Gallery.class, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Gallery> getAll() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Gallery> galleryList = session.createQuery("FROM Gallery g ORDER BY g.id DESC").list();
		return galleryList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Gallery> searchGallery(String query) {
		EntityManager entityManager = this.sessionFactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(Gallery.class)
				.get();
		Query searchQuery = queryBuilder
	            .keyword()
	            .wildcard()
	            .onFields("title", "description")
	            .matching(query + "*")
	            .createQuery();
		FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(searchQuery, Gallery.class);
        return (List<Gallery>)jpaQuery.getResultList();
	}

	@Override
	@Transactional
	public Gallery save(Gallery gallery) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(gallery);
		return gallery;
	}

	@Override
	@Transactional
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Gallery gallery = (Gallery)session.load(Gallery.class, id);
		if (null != gallery) {
			session.delete(gallery);
		}
	}
}
