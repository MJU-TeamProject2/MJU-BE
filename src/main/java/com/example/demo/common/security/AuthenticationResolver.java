package com.example.demo.common.security;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.demo.common.util.auth.Role;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(JwtInfo.class) && parameter.hasParameterAnnotation(AuthInfo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info(authentication.getPrincipal().toString());
		Long id = (Long) authentication.getPrincipal();
		Role role = authentication.getAuthorities().stream()
			.findFirst()
			.map(authority -> Role.valueOf(authority.getAuthority()))
			.orElseThrow(() -> new IllegalStateException("권한이 없습니다."));

		return new JwtInfo(id, role);
	}
}
