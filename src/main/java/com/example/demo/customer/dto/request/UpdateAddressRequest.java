package com.example.demo.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "Update Request", description = "주소 수정 요청")
public record UpdateAddressRequest(
    @Schema(description = "주소 ID", example = "1")
    @Min(value = 0, message = "0 이상의 값을 입력해주세요.")
    @Max(value = Long.MAX_VALUE, message = "최대 " + Long.MAX_VALUE + "까지 입력할 수 있습니다.")
    @NotNull(message = "주소 ID는 필수 입력값입니다.")
    Long addressId,

    @Schema(description = "주소 별칭", example = "회사")
    @NotBlank(message = "주소 별칭은 필수 입력값입니다.")
    @Size(max = 50, message = "주소 별칭은 최대 50자까지 입력 가능합니다.")
    String name,

    @Schema(description = "수령인", example = "홍길동")
    @NotBlank(message = "수령인은 필수 입력값입니다.")
    @Size(max = 50, message = "수령인은 최대 50자까지 입력 가능합니다.")
    String recipient,

    @Schema(description = "우편번호", example = "06234")
    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자로 입력해주세요.")
    String zipCode,

    @Schema(description = "기본주소", example = "서울특별시 강남구 테헤란로 123")
    @NotBlank(message = "기본주소는 필수 입력값입니다.")
    @Size(max = 100, message = "기본주소는 최대 100자까지 입력 가능합니다.")
    String baseAddress,

    @Schema(description = "상세주소", example = "456호")
    @Size(max = 100, message = "상세주소는 최대 100자까지 입력 가능합니다.")
    String detailAddress
) {
}
