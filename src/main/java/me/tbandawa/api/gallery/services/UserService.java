package me.tbandawa.api.gallery.services;

import me.tbandawa.api.gallery.requests.LoginRequest;
import me.tbandawa.api.gallery.requests.RegisterRequest;
import me.tbandawa.api.gallery.responses.AuthResponse;
import me.tbandawa.api.gallery.responses.UserResponse;

public interface UserService {
	AuthResponse signUpUser(RegisterRequest request);
    AuthResponse signInUser(LoginRequest request);
    AuthResponse editUserProfile(RegisterRequest request);
    UserResponse getUserProfile(Long id);
    String logoutUser(String token);
}
