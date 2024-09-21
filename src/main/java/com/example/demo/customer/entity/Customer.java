package com.example.demo.customer.entity;

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
public class Customer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long id;
	private String name;
	private int age;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private String email;
	private String password;
	@Column(name = "phone_number")
	private String phoneNumber;
	private boolean deleted;

	@Builder
	public Customer(String name, int age, Gender gender, String email, String password, String phoneNumber) {
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.deleted = false;
	}
}
