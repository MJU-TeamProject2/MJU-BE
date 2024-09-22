package com.example.demo.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "LoginRequest", description = "Customer 로그인 요청")
public record LoginRequest(@Schema(description = "이메일", requiredMode = Schema.RequiredMode.REQUIRED,
	example = "1234@naver.com")
						   @NotBlank(message = "이메일은 필수입니다.")
						   @Email(message = "올바른 이메일 형식이 아닙니다.")
						   @Size(max = 200, message = "이메일은 200자를 초과할 수 없습니다.")
						   String email,
						   @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
						   @NotBlank(message = "비밀번호는 필수입니다.")
						   @Size(max = 200, message = "비밀번호는 200자를 초과할 수 없습니다.")
						   String password) {
}
