package com.example.demo.customer.dto.response;

import com.example.demo.customer.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(name = "Get Address Response", description = "주소 조회 응답")
public record GetAddressResponse(
    Long addressId,
    String name,
    String recipient,
    String baseAddress,
    String zipCode
) {
  public static List<GetAddressResponse> listOf(List<Address> addresses) {
    return addresses.stream()
        .map(address -> GetAddressResponse.builder()
            .addressId(address.getId())
            .name(address.getName())
            .recipient(address.getRecipient())
            .zipCode(address.getZipCode())
            .baseAddress(address.getBaseAddress()))
        .map(GetAddressResponse.GetAddressResponseBuilder::build)
        .toList();
  }
}
