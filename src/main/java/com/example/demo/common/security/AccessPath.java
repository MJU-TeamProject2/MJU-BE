package com.example.demo.common.security;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.Getter;

@Component
@Getter
public final class AccessPath {

	private final MultiValueMap<String, HttpMethod> customerAllowedPath;
	private final MultiValueMap<String, HttpMethod> adminAllowedPath;

	private AccessPath() {
		customerAllowedPath = initMemberAllowedPath();
		adminAllowedPath = initAdminAllowedPath();
	}

	private MultiValueMap<String, HttpMethod> initMemberAllowedPath() {
		MultiValueMap<String, HttpMethod> customerAllowedPath = new LinkedMultiValueMap<>();
		// review
		customerAllowedPath.put("/api/v1/customer/profile", List.of(HttpMethod.GET, HttpMethod.PATCH));
		customerAllowedPath.put("/api/v1/carts", List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PATCH));
		customerAllowedPath.put("/api/v1/carts/{cartId}", List.of(HttpMethod.DELETE));
		return customerAllowedPath;
	}

	private MultiValueMap<String, HttpMethod> initAdminAllowedPath() {
		MultiValueMap<String, HttpMethod> adminAllowedPath = new LinkedMultiValueMap<>();
		// clothes
		adminAllowedPath.put("/api/v1/clothes/product", List.of(HttpMethod.POST));
		adminAllowedPath.put("/api/v1/admin/test", List.of(HttpMethod.GET));
		adminAllowedPath.put("/api/v1/clothes/{clothesId}", List.of(HttpMethod.PATCH, HttpMethod.DELETE));
		return adminAllowedPath;
	}
}
