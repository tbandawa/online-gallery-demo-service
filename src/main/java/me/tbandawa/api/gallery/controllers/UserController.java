package me.tbandawa.api.gallery.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.tbandawa.api.gallery.entities.Images;
import me.tbandawa.api.gallery.requests.LoginRequest;
import me.tbandawa.api.gallery.requests.RegisterRequest;
import me.tbandawa.api.gallery.responses.AuthResponse;
import me.tbandawa.api.gallery.responses.LogoutResponse;
import me.tbandawa.api.gallery.responses.UserResponse;
import me.tbandawa.api.gallery.services.ImageService;
import me.tbandawa.api.gallery.services.UserDetailsImpl;
import me.tbandawa.api.gallery.services.UserService;

@RestController
@RequestMapping("/api")
@Tag(name = "user", description = "register, login, edit and view user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ImageService imageService;
	
	@Operation(summary = "login user", description = "log in user using usernamr and email", tags = { "user" })
	@PostMapping("/auth/signin")
	public ResponseEntity<AuthResponse> signInUser(@Valid @RequestBody LoginRequest loginRequest) {
		AuthResponse authResponse = userService.signInUser(loginRequest);
		URI profileUri = ServletUriComponentsBuilder
				.fromUriString("/user/profile")
				.path(authResponse.getId().toString())
				.buildAndExpand("")
				.toUri();
		return ResponseEntity.created(profileUri).body(authResponse);
	}

	@Operation(summary = "register user", description = "register user with request-body", tags = { "user" })
	@PostMapping("/auth/signup")
	public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		AuthResponse authResponse = userService.signUpUser(registerRequest);
		return ResponseEntity.ok(authResponse);
	}
	
	@Operation(summary = "edit user details", description = "edit user info with request-body", tags = { "user" })
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/user")
    public ResponseEntity<AuthResponse> editProfile(
    		Authentication authentication,
    		@Valid @RequestBody RegisterRequest registerRequest) {
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		registerRequest.setUserId(userDetails.getId());
        return ResponseEntity.accepted().body(userService.editUserProfile(registerRequest));
    }
	
	@Operation(summary = "upload user photo", description = "upload user photo using multipart", tags = { "user" })
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Images> uploadProfilePhoto(
			Authentication authentication,
			@RequestPart(value = "profile_photo", required = true) MultipartFile profile_photo) {
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		return ResponseEntity.ok().body(imageService.saveProfilePhoto(userDetails.getId(), profile_photo));
	}
	
	@Operation(summary = "get user profile", description = "get user info by id", tags = { "user" })
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserProfile(id));
    }
	
	@Operation(summary = "log out user", description = "log out user and expire token", tags = { "user" })
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logoutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		LogoutResponse logoutResponse = new LogoutResponse();
		logoutResponse.setToken(userService.logoutUser(token));
		return ResponseEntity.ok().body(logoutResponse);
    }
}
