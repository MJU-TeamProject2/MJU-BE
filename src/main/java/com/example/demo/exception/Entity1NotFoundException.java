package com.example.demo.exception;

public class Entity1NotFoundException extends CustomException{//예시를 위한 Exception Class
	public Entity1NotFoundException() {
		super(ExampleErrorCode.ENTITY1_NOT_FOUND);
	}
}
