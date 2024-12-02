package com.example.quiz11.vo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FillinReq {
	private int quizId;

	private String userName;

	private String phone;

	private String email;

	private int age;

	private LocalDate fillinDate;

	// 題號跟選項
	private Map<Integer, List<String>> answer;

	public FillinReq() {
		super();
	}

	public FillinReq(int quizId, String userName, String phone, String email, int age, LocalDate fillinDate,
			Map<Integer, List<String>> answer) {
		super();
		this.quizId = quizId;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.fillinDate = fillinDate;
		this.answer = answer;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public Map<Integer, List<String>> getAnswer() {
		return answer;
	}

}
