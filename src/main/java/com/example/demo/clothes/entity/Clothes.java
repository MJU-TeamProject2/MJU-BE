package com.example.demo.clothes.entity;

import com.example.demo.common.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@FieldNameConstants
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Clothes extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clothes_id")
	private Long id;
	private String category; //todo Category 확정 시 enum
	private String imageUrl;
	@Column(name = "c_name")
	private String name;
	private Integer price;
	@Enumerated(EnumType.STRING)
	private GenderCategory genderCategory;
	private String productNumber;
	private Integer discount;
	private String detailUrl;

	@Builder
	public Clothes(Long id, String category, String imageUrl, String name, Integer price,
		GenderCategory genderCategory, String productNumber, Integer discount, String detailUrl) {
		this.id = id;
		this.category = category;
		this.imageUrl = imageUrl;
		this.name = name;
		this.price = price;
		this.genderCategory = genderCategory;
		this.productNumber = productNumber;
		this.discount = discount;
		this.detailUrl = detailUrl;
	}
}
