package com.example.demo.common.util.auth;

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
public class Auth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_auth_id", columnDefinition = "INTEGER")
	private Integer id;

	private String refreshToken;

	private String code;

	public Auth(String refreshToken, String code) {
		this.refreshToken = refreshToken;
		this.code = code;
	}

	public static Auth createAuth(String code) {
		return new Auth(null, code);
	}

	public void updateRefreshToken(String newRefreshToken) {
		this.refreshToken = newRefreshToken;
	}

	public void logout() {
		this.refreshToken = null;
	}

	public void update(String email) {
		this.code = email;
	}
}
