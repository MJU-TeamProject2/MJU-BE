package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements TestControllerInterface{

	@Override
	public String hello() {
		return "TEST SUCCESS";
	}
}
