package com.example.quiz11.vo;

import java.time.LocalDate;

public class FeedbackDto {
	private int quizId;

	private LocalDate fillinDate;

	private String quizName;

	private String quizDesc;

	private String username;

	private String phone;

	private String email;

	private int age;

	private int quesId;

	private String quesName;

	private String answerStr;

	public FeedbackDto() {
		super();
	}

	public FeedbackDto(int quizId, LocalDate fillinDate, String quizName, String quizDesc, String username,
			String phone, String email, int age, int quesId, String quesName, String answerStr) {
		super();
		this.quizId = quizId;
		this.fillinDate = fillinDate;
		this.quizName = quizName;
		this.quizDesc = quizDesc;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quesId = quesId;
		this.quesName = quesName;
		this.answerStr = answerStr;
	}

	public int getQuizId() {
		return quizId;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public String getQuizName() {
		return quizName;
	}

	public String getQuizDesc() {
		return quizDesc;
	}

	public String getUsername() {
		return username;
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

	public int getQuesId() {
		return quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public String getAnswerStr() {
		return answerStr;
	}

}
