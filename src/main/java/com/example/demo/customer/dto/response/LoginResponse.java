package com.example.demo.customer.dto.response;

import com.example.demo.common.util.auth.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "LoginResponse", description = "로그인 응답")
public record LoginResponse(@Schema(description = "로그인한 유저 id", requiredMode = Schema.RequiredMode.REQUIRED)
							Long customerId,
							@Schema(description = "Access Token", requiredMode = Schema.RequiredMode.REQUIRED)
							String accessToken,
							@Schema(description = "Refresh Token", requiredMode = Schema.RequiredMode.REQUIRED)
							String refreshToken,
							@Schema(description = "Role", requiredMode = Schema.RequiredMode.REQUIRED)
							Role role) {
}
