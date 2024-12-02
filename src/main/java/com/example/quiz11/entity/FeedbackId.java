package com.example.quiz11.entity;

import java.io.Serializable;

public class FeedbackId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int quizId;
	private int quesId;
	private String email;

	public FeedbackId() {
		super();
	}

	public FeedbackId(int quizId, int quesId, String email) {
		super();
		this.quizId = quizId;
		this.quesId = quesId;
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getQuizId() {
		return quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getEmail() {
		return email;
	}

}
