package com.example.demo.customer.dto.response;

import com.example.demo.customer.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Get Address Detail Response", description = "주소 상세 조회 응답")
public record GetAddressDetailResponse(
    Long addressId,
    String name,
    String zipCode,
    String baseAddress,
    String detailAddress
) {
  public static GetAddressDetailResponse from(Address address) {
    return GetAddressDetailResponse.builder()
        .addressId(address.getId())
        .name(address.getName())
        .zipCode(address.getZipCode())
        .baseAddress(address.getBaseAddress())
        .detailAddress(address.getDetailAddress())
        .build();
  }
}
