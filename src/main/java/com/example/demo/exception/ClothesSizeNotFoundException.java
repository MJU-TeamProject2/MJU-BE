package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class ClothesSizeNotFoundException extends CustomException {
	public ClothesSizeNotFoundException() {
		super(ClothesErrorCode.CLOTHES_SIZE_NOT_FOUND);
	}
}
