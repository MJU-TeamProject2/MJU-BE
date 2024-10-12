package com.example.demo.common.security;

import com.example.demo.common.util.auth.Role;

public record JwtInfo(Long memberId, Role role) {
}
