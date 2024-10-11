package com.example.demo.admin.dto.response;

import com.example.demo.common.util.auth.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "AdminLoginResponse", description = "관리자 로그인 응답")
public record AdminLoginResponse(@Schema(description = "로그인한 관리자 이름", requiredMode = Schema.RequiredMode.REQUIRED)
								 String name,
								 @Schema(description = "Access Token", requiredMode = Schema.RequiredMode.REQUIRED)
								 String accessToken,
								 @Schema(description = "Refresh Token", requiredMode = Schema.RequiredMode.REQUIRED)
								 String refreshToken,
								 @Schema(description = "Role", requiredMode = Schema.RequiredMode.REQUIRED)
								 Role role) {
}
