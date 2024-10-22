package com.example.demo.exception;

import com.example.demo.common.exception.CustomException;

public class ClothesInsufficientStockException extends CustomException {

  public ClothesInsufficientStockException() {
    super(ClothesErrorCode.CLOTHES_INSUFFICIENT_STOCK);
  }
}
