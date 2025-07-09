package com.mhp.auth_service.controller;

import com.mhp.auth_service.dto.LoginRequestDTO;
import com.mhp.auth_service.dto.LoginResponseDTO;
import com.mhp.auth_service.dto.RegisterRequestDTO;
import com.mhp.auth_service.model.User;
import com.mhp.auth_service.repository.UserRepository;
import com.mhp.auth_service.service.AuthService;
import com.mhp.auth_service.util.JwtUtil;
import com.mhp.auth_service.util.Role;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "AuthController", description = "API for authenticate and manage accounts")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService,
                          UserRepository userRepository) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user in auth_service database")
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        Optional<User> optionalUser = authService.register(registerRequestDTO);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(optionalUser.get());
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        // Authorization: Bearer <token>
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!authService.validateToken(authHeader.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims jwtClaims = authService.getJwtClaims(authHeader.substring(7));
        User user = new User();
        user.setEmail(jwtClaims.getSubject());
        user.setRole((Role) jwtClaims.get("role"));
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/user/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
