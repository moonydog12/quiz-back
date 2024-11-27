package com.example.quiz11.vo;

public class BasicRes {
	private int code;
	private String message;

	public BasicRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BasicRes(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
