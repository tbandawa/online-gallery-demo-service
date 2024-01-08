package me.tbandawa.api.gallery.daos;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
import me.tbandawa.api.gallery.entities.PagedGallery;

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
	
	@Override
	@Transactional(readOnly = true)
	public PagedGallery getGalleries(int pageNumber) {
		PagedGallery pagedGallery = new PagedGallery();
		pagedGallery.setGallaries(Arrays.asList());
		pagedGallery.setPerPage(10);
		pagedGallery.setCurrentPage(pageNumber);
		pageNumber = pageNumber - 1;
		
		Session session = this.sessionFactory.getCurrentSession();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        
        countQuery.select(criteriaBuilder.count(countQuery.from(Gallery.class)));
        Long count = session.createQuery(countQuery).getSingleResult();
        pagedGallery.setCount(count.intValue());
        
        CriteriaQuery<Gallery> criteriaQuery = criteriaBuilder.createQuery(Gallery.class);
        Root<Gallery> root = criteriaQuery.from(Gallery.class);
        CriteriaQuery<Gallery> selectQuery = criteriaQuery.select(root);
        
        TypedQuery<Gallery> typedQuery = session.createQuery(selectQuery);
		
        if (pageNumber < count.intValue()) {
        	typedQuery.setFirstResult(pageNumber * 10);
            typedQuery.setMaxResults(10);
            
            pagedGallery.setGallaries(typedQuery.getResultList());
            pagedGallery.setNextPage(pageNumber + 2);
        }
        
		return pagedGallery;
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
