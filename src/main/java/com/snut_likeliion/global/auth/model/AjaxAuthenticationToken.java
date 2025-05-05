package com.snut_likeliion.global.auth.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AjaxAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credentials;

    public AjaxAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public AjaxAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    public static AjaxAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new AjaxAuthenticationToken(principal, credentials);
    }

    public static AjaxAuthenticationToken authenticated(SnutLikeLionUser principal) {
        return new AjaxAuthenticationToken(principal, null, principal.getAuthorities());
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
