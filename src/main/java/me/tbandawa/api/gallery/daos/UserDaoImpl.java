package me.tbandawa.api.gallery.daos;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.tbandawa.api.gallery.entities.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public Long addUser(User user) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Long)session.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> getUser(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return Optional.ofNullable(session.get(User.class, id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<?> query = session.createQuery("FROM User WHERE username = :username");
		query.setParameter("username", username);
		User results = (User)query.uniqueResult();
		return results == null? Optional.empty() : Optional.of(results);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByEmail(String email) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<?> query = session.createQuery("FROM User WHERE email = :email");
		query.setParameter("email", email);
		User results = (User)query.uniqueResult();
		return results == null? Optional.empty() : Optional.of(results);
	}

	@Override
	@Transactional
	public int editUser(User user) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<?> query = session.createQuery("UPDATE User SET username = :username, email = :email, firstname = :firstname, lastname = :lastname, password = :password WHERE id = :id");
		query.setParameter("username", user.getUsername());
		query.setParameter("email", user.getEmail());
		query.setParameter("firstname", user.getFirstname());
		query.setParameter("lastname", user.getLastname());
		query.setParameter("password", user.getPassword());
		query.setParameter("id", user.getId());
		return query.executeUpdate();
	}
}
