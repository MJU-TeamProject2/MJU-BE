package com.example.demo.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "AdminLoginRequest", description = "Admin 로그인 요청")
public record AdminLoginRequest(@Schema(description = "관리자 코드", requiredMode = Schema.RequiredMode.REQUIRED,
								example = "Admin001")
								@NotBlank(message = "관리자 코드는 필수입니다.")
								String code,
								@Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED,
								example = "admin1")
								@NotBlank(message = "비밀번호는 필수입니다.")
								@Size(max = 200, message = "비밀번호는 200자를 초과할 수 없습니다.")
								String password) {
}
