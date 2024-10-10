package com.example.demo.common.util.auth;

import org.springframework.stereotype.Service;

import com.example.demo.exception.CustomerAuthNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository authRepository;

	public void createAuth(String code) {
		authRepository.save(Auth.createAuth(code));
	}

	public Auth findByCode(String code) {
		return authRepository.findByCode(code)
			.orElseThrow(CustomerAuthNotFoundException::new);
	}
}
