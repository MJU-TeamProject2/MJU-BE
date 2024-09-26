package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class ClothesNotFoundException extends CustomException {
	public ClothesNotFoundException() {
		super(ClothesErrorCode.CLOTHES_NOT_FOUND);
	}
}
