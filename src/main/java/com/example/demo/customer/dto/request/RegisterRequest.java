package com.example.demo.customer.dto.request;

import com.example.demo.customer.entity.Gender;
import com.example.demo.customer.util.GenderValidation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "RegisterRequest", description = "Customer 회원가입 요청")
public record RegisterRequest(@Schema(description = "이름", requiredMode = Schema.RequiredMode.REQUIRED)
							  @NotBlank(message = "이름은 필수입니다.")
	  						  @Size(max = 10, message = "이름은 10자를 초과할 수 없습니다.")
							  String name,
							  @Schema(description = "나이", requiredMode = Schema.RequiredMode.REQUIRED)
							  @NotNull(message = "나이는 필수입니다.")
							  @Positive(message = "나이는 0보다 커야 합니다.")
							  int age,
							  @Schema(description = "성별", requiredMode = Schema.RequiredMode.REQUIRED,
								  allowableValues = {"M", "F", "남자", "여자"})
							  @NotNull(message = "성별은 필수입니다.")
							  @GenderValidation.ValidGender
							  Gender gender,
							  @Schema(description = "이메일", requiredMode = Schema.RequiredMode.REQUIRED,
							  	example = "1234@naver.com")
							  @NotBlank(message = "이메일은 필수입니다.")
							  @Email(message = "올바른 이메일 형식이 아닙니다.")
							  @Size(max = 200, message = "이메일은 200자를 초과할 수 없습니다.")
							  String email,
							  @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
							  @NotBlank(message = "비밀번호는 필수입니다.")
							  @Size(max = 200, message = "비밀번호는 200자를 초과할 수 없습니다.")
							  String password,
							  @Schema(description = "전화번호", requiredMode = Schema.RequiredMode.REQUIRED)
							  @NotBlank(message = "전화번호는 필수입니다.")
							  @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 xxx-xxxx-xxxx 형식이어야 합니다")
							  String phoneNumber) {
}
