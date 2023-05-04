package me.tbandawa.api.gallery.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.tbandawa.api.gallery.requests.LoginRequest;
import me.tbandawa.api.gallery.requests.RegisterRequest;
import me.tbandawa.api.gallery.responses.AuthResponse;
import me.tbandawa.api.gallery.responses.UserResponse;
import me.tbandawa.api.gallery.services.UserService;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "user", description = "register, login and view user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
	@Autowired
	UserService userService;
	
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

	@PostMapping("/auth/signup")
	public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
		log.info("request -> {}", signUpRequest.toString());
		//AuthResponse authResponse = userService.signUpUser(signUpRequest);
		//return ResponseEntity.ok(authResponse);
		return null;
	}
	
	@GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable Long id)  {
        return ResponseEntity.ok().body(userService.getUserProfile(id));
    }
}
