package com.mhp.api_gateway.config.auth;

import com.mhp.api_gateway.dto.UserDTO;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthManager implements ReactiveAuthenticationManager{

    private final WebClient webClient;

    public AuthManager(WebClient.Builder builder, @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = builder.baseUrl(authServiceUrl).build();
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        return webClient.get()
                .uri("/validate") // assumes this returns user info if valid
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.error(new BadCredentialsException("Invalid Token")))
                .bodyToMono(UserDTO.class) // your DTO with username + roles
                .map(user -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
                    return new JwtAuthToken(user.getEmail(), authorities);
                });
    }
}
