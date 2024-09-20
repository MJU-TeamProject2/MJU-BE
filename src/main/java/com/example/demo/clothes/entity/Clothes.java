package com.example.demo.clothes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Clothes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clothes_id")
	private Long id;
	private String category; //todo Category 확정 시 enum
	private String imageUrl;
	@Column(name = "c_name")
	private String name;
	private Integer price;
	private String detail;
	private String detailUrl;
	private Integer quantity;

	@Builder
	public Clothes(Long id, String category, String imageUrl, String name, Integer price,
		String detail, String detailUrl, Integer quantity) {
		this.id = id;
		this.category = category;
		this.imageUrl = imageUrl;
		this.name = name;
		this.price = price;
		this.detail = detail;
		this.detailUrl = detailUrl;
		this.quantity = quantity;
	}
}
