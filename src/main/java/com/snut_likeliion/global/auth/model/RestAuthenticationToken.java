package com.snut_likeliion.global.auth.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RestAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credentials;

    public RestAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public RestAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    public static RestAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new RestAuthenticationToken(principal, credentials);
    }

    public static RestAuthenticationToken authenticated(SnutLikeLionUser principal) {
        return new RestAuthenticationToken(principal, null, principal.getAuthorities());
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
