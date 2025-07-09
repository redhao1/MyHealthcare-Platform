package com.mhp.api_gateway.config;

import com.mhp.api_gateway.config.auth.AuthConverter;
import com.mhp.api_gateway.config.auth.AuthManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final AuthManager authManager;

    public SecurityConfig(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authWebFilter = new AuthenticationWebFilter(authManager);
        authWebFilter.setServerAuthenticationConverter(new AuthConverter());

        http
                .securityMatcher(ServerWebExchangeMatchers.anyExchange())
                .addFilterAt(authWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/auth/**", "/api-docs/**", "/actuator/**").permitAll()
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        .anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }
}
