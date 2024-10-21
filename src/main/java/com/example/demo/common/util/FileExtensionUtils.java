package com.example.demo.common.util;

import java.util.Map;
import java.util.Optional;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileExtensionUtils {

	public static final Map<String, String> IMAGE_EXTENSIONS = Map.of(
		"jpg", "image/jpg",
		"jpeg", "image/jpeg",
		"png", "image/png",
		"gif", "image/gif",
		"bmp", "image/bmp",
		"webp", "image/webp",
		"tiff", "image/tiff",
		"svg", "image/svg+xml"
	);

	public static boolean isImage(String filename) {
		String extension = getExtension(filename);
		return IMAGE_EXTENSIONS.containsKey(extension);
	}

	public static String getContentType(String filename) {
		String extension = getExtension(filename);
		if (IMAGE_EXTENSIONS.containsKey(extension)) {
			return IMAGE_EXTENSIONS.get(extension);
		} else if (extension.contains("obj")) {
			return "object.obj";
		} else {
			throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
		}
	}

	private static String getExtension(String filename) {
		return Optional.ofNullable(StringUtils.getFilenameExtension(filename))
			.orElseThrow(() -> new IllegalArgumentException("확장자가 없는 파일은 업로드할 수 없습니다."));
	}
}
