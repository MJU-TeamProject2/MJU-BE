package com.example.demo.common.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
	private final Long id;

	public JwtAuthenticationToken(Long id, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(true);
		this.id = id;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return id;
	}
}
