package com.example.demo.admin.entity;

import com.example.demo.common.util.BaseEntity;
import com.example.demo.common.util.auth.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@FieldNameConstants
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private Long id;

	private String code;
	private String password;
	private String name;

	@Builder
	public Admin(String code, String password, String name) {
		this.code = code;
		this.password = password;
		this.name = name;
	}

	public Role getRole() {return Role.ADMIN;}


}
