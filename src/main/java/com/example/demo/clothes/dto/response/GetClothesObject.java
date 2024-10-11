package com.example.demo.clothes.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Clothes Object Response", description = "특정 의류 obj 파일 조회 응답")
public record GetClothesObject(@Schema(description = "의류 id", requiredMode = Schema.RequiredMode.REQUIRED)
							   Long clothesId,
							   @Schema(description = "의류 obj 파일", requiredMode = Schema.RequiredMode.REQUIRED)
							   byte[] file,
							   @Schema(description = "파일 이름", requiredMode = Schema.RequiredMode.REQUIRED)
							   String fileName) {

	public static GetClothesObject of(Long clothesId, byte[] file, String fileName) {
		return GetClothesObject.builder()
			.clothesId(clothesId)
			.file(file)
			.fileName(fileName)
			.build();
	}
}
