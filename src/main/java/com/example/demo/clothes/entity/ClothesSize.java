package com.example.demo.clothes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@FieldNameConstants
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesSize {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clothes_size_id")
	private Long clothesSizeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clothes_id")
	private Clothes clothes;

	@Enumerated(EnumType.STRING)
	private Size size;

	private int quantity;

	@Builder
	public ClothesSize(Long clothesSizeId, Clothes clothes, Size size, int quantity) {
		this.clothesSizeId = clothesSizeId;
		this.clothes = clothes;
		this.size = size;
		this.quantity = quantity;
	}
}
