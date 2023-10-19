package me.tbandawa.api.gallery.daos;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.tbandawa.api.gallery.entities.UserToken;

@Repository
public class TokenDaoImpl implements TokenDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional
	public UserToken addToken(UserToken userToken) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(userToken);
		return userToken;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<UserToken> getToken(long userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<?> query = session.createQuery("FROM UserToken u WHERE u.userId = :userId");
		query.setParameter("userId", userId);
		return (List<UserToken>)query.getResultList();
	}

	@Override
	@Transactional
	public int deleteToken(long userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Query<?> query = session.createQuery("DELETE FROM UserToken WHERE userId = :userId");
		query.setParameter("userId", userId);
		return query.executeUpdate();
	}
}