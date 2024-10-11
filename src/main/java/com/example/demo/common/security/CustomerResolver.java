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
public class CustomerResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(JwtInfo.class) && parameter.hasParameterAnnotation(AuthInfo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		// Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//
		// if (authentication == null || !authentication.isAuthenticated()) {
		// 	throw new IllegalStateException("인증되지 않은 사용자입니다.");
		// }
		//
		// Object principal = authentication.getPrincipal();
		// long customerId;
		//
		// if (principal instanceof String) {
		// 	try {
		// 		customerId = Long.parseLong((String) principal);
		// 	} catch (NumberFormatException e) {
		// 		throw new IllegalStateException("유효하지 않은 고객 ID 형식입니다.", e);
		// 	}
		// } else if (principal instanceof Long) {
		// 	customerId = (Long) principal;
		// } else {
		// 	throw new IllegalStateException("지원되지 않는 Principal 타입입니다: " + principal.getClass());
		// }
		//
		// return new JwtInfo(customerId, Role.CUSTOMER);
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
