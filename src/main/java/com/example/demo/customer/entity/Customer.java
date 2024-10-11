package com.example.demo.customer.entity;

import org.springframework.util.StringUtils;

import com.example.demo.common.util.BaseEntity;
import com.example.demo.common.util.auth.Role;

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
	@Column(name = "nick_name")
	private String nickName;
	private int age;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private String email;
	private String password;
	@Column(name = "phone_number")
	private String phoneNumber;
	private boolean deleted;

	@Builder
	public Customer(String name, String nickName, int age, Gender gender, String email, String password, String phoneNumber) {
		this.name = name;
		this.age = age;
		this.nickName = StringUtils.hasText(nickName) ? nickName : name;
		this.gender = gender;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.deleted = false;
	}

	public Role getRole() {
		return Role.CUSTOMER;
	}

	public void update(String email, String name, String nickName, int age, String phoneNumber) {
		this.email = email;
		this.name = name;
		this.nickName = nickName;
		this.age = age;
		this.phoneNumber = phoneNumber;
	}
}
