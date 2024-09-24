package com.example.demo.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CustomerAuth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_auth_id", columnDefinition = "INTEGER")
	private Integer id;

	private String refreshToken;

	private Long customerId;

	public CustomerAuth(String refreshToken, Long customerId) {
		this.refreshToken = refreshToken;
		this.customerId = customerId;
	}

	public static CustomerAuth createAuth(Long customerId) {
		return new CustomerAuth(null, customerId);
	}

	public void updateRefreshToken(String newRefreshToken) {
		this.refreshToken = newRefreshToken;
	}

	public void logout() {
		this.refreshToken = null;
	}
}
