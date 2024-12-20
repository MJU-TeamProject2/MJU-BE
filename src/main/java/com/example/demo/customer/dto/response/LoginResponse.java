package com.example.demo.customer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "LoginResponse", description = "로그인 응답")
public record LoginResponse(@Schema(description = "Access Token", requiredMode = Schema.RequiredMode.REQUIRED)
							String accessToken,
							@Schema(description = "Refresh Token", requiredMode = Schema.RequiredMode.REQUIRED)
							String refreshToken,
							@Schema(description = "바디 오브젝트 URL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
							String bodyObjectUrl) {
}
