package me.tbandawa.api.gallery.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import me.tbandawa.api.gallery.daos.RoleDao;
import me.tbandawa.api.gallery.daos.UserDao;
import me.tbandawa.api.gallery.jwt.JwtUtils;
import me.tbandawa.api.gallery.requests.LoginRequest;
import me.tbandawa.api.gallery.requests.RegisterRequest;
import me.tbandawa.api.gallery.responses.AuthResponse;
import me.tbandawa.api.gallery.responses.UserResponse;
import me.tbandawa.api.gallery.entities.User;
import me.tbandawa.api.gallery.entities.Role;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private GalleryMapper galleryMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ImageService imageService;

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;

	@Override
	public AuthResponse signUpUser(RegisterRequest request) {
		
		userDao.findByUsername(request.getUsername()).ifPresent(user -> {
			throw new RuntimeException("Username alread exixts");
		});
		
		userDao.findByEmail(request.getEmail()).ifPresent(user -> {
			throw new RuntimeException("Email alread exixts");
		});
		
		User user = new User();
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		Set<String> requestRoles = request.getRole();
	    Set<Role> roles = new HashSet<>();

	    if (requestRoles == null) {
	      Role userRole = roleDao.findByName("user")
	          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	      roles.add(userRole);
	    } else {
	    	requestRoles.forEach(role -> {
	        switch (role) {
	        case "admin":
	          Role adminRole = roleDao.findByName("admin")
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(adminRole);

	          break;
	        case "mod":
	          Role modRole = roleDao.findByName("mod")
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(modRole);

	          break;
	        default:
	          Role userRole = roleDao.findByName("user")
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(userRole);
	        }
	      });
	    }
		
		user.setRoles(roles);
		
		if (userDao.addUser(user) < 1) {
            throw new RuntimeException("User not created");
        }
		
		return signInUser(new LoginRequest(request.getUsername(), request.getPassword()));
	}

	@Override
	public AuthResponse signInUser(LoginRequest request) {
		
		Authentication authentication = authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		    SecurityContextHolder.getContext().setAuthentication(authentication);
		    String jwt = jwtUtils.generateJwtToken(authentication);
		    
		    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
		    List<String> roles = userDetails.getAuthorities().stream()
		        .map(item -> item.getAuthority())
		        .collect(Collectors.toList());

		return new AuthResponse(jwt,
				userDetails.getId(),
				userDetails.getFirstname(),
				userDetails.getLastname(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles,
				imageService.getProfilePhoto(userDetails.getId())
			);
	}
	
	@Override
	public AuthResponse editUserProfile(RegisterRequest request) {
		
		userDao.findByUsername(request.getUsername()).ifPresent(user -> {
			throw new RuntimeException("Username alread exixts");
		});
		
		userDao.findByEmail(request.getEmail()).ifPresent(user -> {
			throw new RuntimeException("Email alread exixts");
		});
		
		User user = new User();
		user.setId(request.getUserId());
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		if (userDao.editUser(user) == 0) {
			throw new RuntimeException("Could not save changes");
		}
		
		return signInUser(new LoginRequest(request.getUsername(), request.getPassword()));
	}

	@Override
	public UserResponse getUserProfile(Long id) {
		
		User user = userDao.getUser(id)
				.orElseThrow(() -> new RuntimeException("Error: User is not found."));
		
		user.getGallery().forEach(gallery -> {
			gallery.setImages(imageService.getImages(gallery.getId()));
		});
		
		return new UserResponse(
				user.getId(),
				user.getFirstname(),
				user.getLastname(),
				user.getUsername(),
				user.getEmail(),
				galleryMapper.mapToGalleryResponse(user.getGallery()),
				imageService.getProfilePhoto(user.getId())
			);
	}
}
