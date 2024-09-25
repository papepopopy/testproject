package com.spring.boardtest;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;

    public CustomAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null; // 비밀번호가 없으므로 null
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
