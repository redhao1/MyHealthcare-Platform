package com.mhp.api_gateway.config.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthToken extends AbstractAuthenticationToken {
    private final String token;
    private final Object principal;

    public JwtAuthToken(String token) {
        super(null);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    public JwtAuthToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = null;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
