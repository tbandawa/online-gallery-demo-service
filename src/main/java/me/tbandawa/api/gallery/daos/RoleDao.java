package me.tbandawa.api.gallery.daos;

import java.util.Optional;

import me.tbandawa.api.gallery.models.Role;


public interface RoleDao {
	Optional<Role> findByName(String name);
}
