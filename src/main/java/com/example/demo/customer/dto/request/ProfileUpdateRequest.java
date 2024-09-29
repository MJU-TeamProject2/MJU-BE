package com.example.demo.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "ProfileUpdateRequest", description = "Customer 정보 수정 요청")
public record ProfileUpdateRequest(@Schema(description = "이름", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
								   @Size(max = 10, message = "이름은 10자를 초과할 수 없습니다.")
								   String name,
								   @Schema(description = "닉네임", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
									   example = "nickName")
								   String nickName,
								   @Schema(description = "나이", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
									   example = "25")
								   @Positive(message = "나이는 0보다 커야 합니다.")
								   int age,
								   @Schema(description = "이메일", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
									   example = "1234@naver.com")
								   @Email(message = "올바른 이메일 형식이 아닙니다.")
								   @Size(max = 200, message = "이메일은 200자를 초과할 수 없습니다.")
								   String email,
								   @Schema(description = "전화번호", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
								   @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 xxx-xxxx-xxxx 형식이어야 합니다")
								   String phoneNumber) {
}
