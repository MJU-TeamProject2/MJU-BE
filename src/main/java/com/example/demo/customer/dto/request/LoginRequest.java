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

@Schema(name = "LoginRequest", description = "Customer 로그인 요청")
public record LoginRequest() {
}
