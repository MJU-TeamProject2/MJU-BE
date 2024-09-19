package com.example.demo.common.util;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
