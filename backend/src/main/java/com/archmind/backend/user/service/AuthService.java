package com.archmind.backend.user.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.archmind.backend.common.response.ApiResponse;
import com.archmind.backend.security.JwtService;
import com.archmind.backend.user.dto.LoginRequest;
import com.archmind.backend.user.dto.LoginResponse;
import com.archmind.backend.user.dto.RegisterRequest;
import com.archmind.backend.user.dto.UserResponse;
import com.archmind.backend.user.entity.User;
import com.archmind.backend.user.repository.UserRepository;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public ApiResponse login(LoginRequest request) {

        System.out.println("========== LOGIN ==========");
        System.out.println("Email entered: " + request.getEmail());
        System.out.println("Password entered: " + request.getPassword());

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        System.out.println("User found: " + (user != null));

        if (user == null) {
            return new ApiResponse(
                    false,
                    "Invalid email or password",
                    null
            );
        }

        System.out.println("DB Email: " + user.getEmail());
        System.out.println("DB Password: " + user.getPassword());

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        System.out.println("Password matches: " + matches);

        if (!matches) {
            return new ApiResponse(
                    false,
                    "Invalid email or password",
                    null
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse(token);

        return new ApiResponse(
                true,
                "Login successful",
                response
        );
    }

    public ApiResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            return new ApiResponse(
                    false,
                    "Email already exists",
                    null
            );
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // Password encryption comes later
        user.setPassword(
        passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());

        return new ApiResponse(
                true,
                "User registered successfully",
                response
        );
    }
}