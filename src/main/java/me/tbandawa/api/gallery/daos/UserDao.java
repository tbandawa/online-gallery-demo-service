package me.tbandawa.api.gallery.daos;

import java.util.Optional;

import me.tbandawa.api.gallery.entities.User;

public interface UserDao {
	Long addUser(User user);
	Optional<User> getUser(Long id);
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
}
