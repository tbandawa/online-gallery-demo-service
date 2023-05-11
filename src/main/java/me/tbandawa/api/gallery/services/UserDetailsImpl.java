package me.tbandawa.api.gallery.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.tbandawa.api.gallery.entities.Role;
import me.tbandawa.api.gallery.entities.User;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;
	  
	private String firstname;
	  
	private String lastname;

	private String username;

	private String email;
	
	private Set<Role> roles;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String firstname, String lastname, String username, String email, Set<Role> roles, String password,
	      Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
	    this.firstname = firstname;
	    this.lastname = lastname;
	    this.username = username;
	    this.email = email;
	    this.roles = roles;
	    this.password = password;
	    this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
	    List<GrantedAuthority> authorities = user.getRoles().stream()
	        .map(role -> new SimpleGrantedAuthority(role.getName()))
	        .collect(Collectors.toList());

	    return new UserDetailsImpl(
	        user.getId(),
	        user.getFirstname(),
	        user.getLastname(),
	        user.getUsername(), 
	        user.getEmail(),
	        user.getRoles(),
	        user.getPassword(), 
	        authorities
	    );
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}
	  
	public String getFirstname() {
		return firstname;
	}
	  
	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	public Set<Role> getRoles() {
		return roles;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
	    UserDetailsImpl user = (UserDetailsImpl) o;
	    return Objects.equals(id, user.id);
	}
}
