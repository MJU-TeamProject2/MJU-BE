package com.example.demo.customer.dto.request;

import com.example.demo.customer.entity.Gender;

import io.swagger.v3.oas.annotations.media.Schema;

// TODO 각 컬럼 valid 조건 확인 필요 &
@Schema(name = "RegisterRequest", description = "Customer 회원가입 요청")
public record RegisterRequest(@Schema(description = "이름", requiredMode = Schema.RequiredMode.REQUIRED)
							  String name,
							  @Schema(description = "나이", requiredMode = Schema.RequiredMode.REQUIRED)
							  int age,
							  @Schema(description = "성별", requiredMode = Schema.RequiredMode.REQUIRED,
								  allowableValues = {"M", "F", "남자", "여자"})
							  Gender gender,
							  @Schema(description = "이메일", requiredMode = Schema.RequiredMode.REQUIRED)
							  String email,
							  @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
							  String password,
							  @Schema(description = "전화번호", requiredMode = Schema.RequiredMode.REQUIRED)
							  String phoneNumber) {
}
