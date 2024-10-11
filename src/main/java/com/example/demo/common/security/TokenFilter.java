package com.example.demo.common.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.common.security.exception.SecurityErrorCode;
import com.example.demo.common.security.exception.TokenException;
import com.example.demo.common.security.exception.TokenExpiredException;
import com.example.demo.common.util.auth.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;
	private final AccessPath accessPath;
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String token = getTokenFromRequest(request);
		try {
			tokenProvider.validateToken(JwtType.ACCESS, token);
		} catch (TokenExpiredException e) {
			// 토큰 만료 처리
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token has expired");
			return;
		} catch (TokenException e) {
			// 유효하지 않은 토큰 처리
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid token");
			return;
		}

		Authentication authentication = tokenProvider.getCustomerIdFromToken(token);
		validateRoleHasAccessPermission(request, authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		throw new TokenException(SecurityErrorCode.NON_BEARER);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !isRequestMatch(accessPath.getCustomerAllowdPath(), request);
	}

	private boolean isRequestMatch(MultiValueMap<String, HttpMethod> ignoredPaths, HttpServletRequest request) {
		return ignoredPaths.keySet().stream()
			.anyMatch(ignoredUri ->
				isMatchUri(ignoredUri, request) && isMatchMethod(ignoredPaths.get(ignoredUri), request));
	}

	private boolean isMatchUri(String ignoredUri, HttpServletRequest requestUri) {
		return antPathMatcher.match(ignoredUri, requestUri.getRequestURI());
	}

	private boolean isMatchMethod(List<HttpMethod> ignoredPathList, HttpServletRequest request) {
		return ignoredPathList.stream().anyMatch(httpMethod -> httpMethod.matches(request.getMethod()));
	}

	private void validateRoleHasAccessPermission(HttpServletRequest request, Authentication authentication) {
		boolean hasAccessPermissionAllowed = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.map(Role::valueOf)
			.anyMatch(authority -> {
				if (authority.equals(Role.CUSTOMER)) {
					return isRequestMatch(accessPath.getCustomerAllowdPath(), request);
				}
				else if(authority.equals(Role.ADMIN)) {
					return isRequestMatch(accessPath.getAdminAllowedPath(), request);
				}
				return false;
			});

		if (!hasAccessPermissionAllowed) {
			throw new TokenException(SecurityErrorCode.AUTHORITY_NOT_FOUND);
		}
	}
}
