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

	private final MultiValueMap<String, HttpMethod> customerAllowdPath;

	private AccessPath() {
		customerAllowdPath = initCustomerAllowedPath();
	}

	private MultiValueMap<String, HttpMethod> initCustomerAllowedPath() {
		MultiValueMap<String, HttpMethod> customerAllowdPath = new LinkedMultiValueMap<>();
		// review
		customerAllowdPath.put("/api/v1/customer/profile", List.of(HttpMethod.GET, HttpMethod.PATCH));

		return customerAllowdPath;
	}

	public MultiValueMap<String, HttpMethod> getAdminAllowedPath() {
		MultiValueMap<String, HttpMethod> adminAllowedPath = new LinkedMultiValueMap<>();
		// adminAllowedPath.put("/api/v1/")
		return adminAllowedPath;
	}
}
