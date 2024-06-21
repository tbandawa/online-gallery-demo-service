package me.tbandawa.api.gallery.daos;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.tbandawa.api.gallery.entities.Role;

@Repository
public class RoleDaoImpl implements RoleDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly = true)
	public Optional<Role> findByName(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<?> query = session.createQuery("FROM Role WHERE name = :name");
		query.setParameter("name", name);
		Role results = (Role)query.uniqueResult();
		return results == null? Optional.empty() : Optional.of(results);
	}
}
