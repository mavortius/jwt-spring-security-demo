package org.zerhusen.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String principal;
    private String credentials;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtAuthenticationToken(String principal, String credentials) {
        this(Collections.emptyList(), principal, credentials);
    }

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String principal, String credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if(super.getAuthorities().isEmpty()) {
            return (Collection<GrantedAuthority>) authorities;
        } else {
            return super.getAuthorities();
        }
    }
}
