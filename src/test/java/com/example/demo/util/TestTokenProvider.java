package com.example.demo.util;

import com.example.demo.common.util.auth.Role;

public class TestTokenProvider {

	public static String createTestAccessToken(Long userId, Role role) {
		return String.format("test_access_token_%d_%s", userId, role.name());
	}

	public static String createTestRefreshToken(Long userId, Role role) {
		return String.format("test_refresh_token_%d_%s", userId, role.name());
	}

}
