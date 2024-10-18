package com.example.demo.clothes.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.common.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	@Enumerated(EnumType.STRING)
	private ClothesCategory category;
	private String imageUrl;
	@Column(name = "c_name")
	private String name;
	private Integer price;
	@Enumerated(EnumType.STRING)
	private GenderCategory genderCategory;
	private String productNumber;
	private Integer discount;
	private String detailUrl;
	@OneToMany(mappedBy = "clothes")
	private List<ClothesSize> clothesSizeList;
	// 의상 object path
	private String objectKey;
	private LocalDateTime deletedAt;

	@Builder
	public Clothes(Long id, ClothesCategory category, String imageUrl, String name, Integer price,
		GenderCategory genderCategory, String productNumber, Integer discount, String detailUrl, String objectKey) {
		this.id = id;
		this.category = category;
		this.imageUrl = imageUrl;
		this.name = name;
		this.price = price;
		this.genderCategory = genderCategory;
		this.productNumber = productNumber;
		this.discount = discount;
		this.detailUrl = detailUrl;
		this.clothesSizeList = new ArrayList<>();
		this.objectKey = objectKey;
		this.deletedAt = null;
	}

	public void update(ClothesCategory category, String imageUrl, String name, Integer price,
		GenderCategory genderCategory, String productNumber, Integer discount, String detailUrl, String objectKey) {
		this.category = category == null ? this.category : category;
		this.imageUrl = imageUrl == null ? this.imageUrl : imageUrl;
		this.name = name == null ? this.name : name;
		this.price = price == null ? this.price : price;
		this.genderCategory = genderCategory == null ? this.genderCategory : genderCategory;
		this.productNumber = productNumber == null ? this.productNumber : productNumber;
		this.discount = discount == null ? this.discount : discount;
		this.detailUrl = detailUrl == null ? this.detailUrl : detailUrl;
		this.objectKey = objectKey == null ? this.objectKey : objectKey;
	}

	public void delete(LocalDateTime time) {
		this.deletedAt = time;
	}
}
