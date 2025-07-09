package com.mhp.auth_service.service;


import com.mhp.auth_service.dto.LoginRequestDTO;
import com.mhp.auth_service.dto.RegisterRequestDTO;
import com.mhp.auth_service.exception.EmailAlreadyExistsException;
import com.mhp.auth_service.exception.UserNotFoundException;
import com.mhp.auth_service.model.User;
import com.mhp.auth_service.repository.UserRepository;
import com.mhp.auth_service.util.JwtUtil;
import com.mhp.auth_service.util.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public Optional<User> register(RegisterRequestDTO registerRequestDTO) {

        if(userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A user with this email " +
                    "already exists: " + registerRequestDTO.getEmail());
        }
        User newUser = new User();
        newUser.setEmail(registerRequestDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        newUser.setRole(Role.valueOf(registerRequestDTO.getRole().toUpperCase()));
        userRepository.save(newUser);
        return Optional.of(newUser);
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userService
                .findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
                        u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole().toString()));

        return token;
    }

    public boolean validateToken(String token) {
        try{
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getJwtClaims(String token) {
        try {
            return jwtUtil.parseToken(token);
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
