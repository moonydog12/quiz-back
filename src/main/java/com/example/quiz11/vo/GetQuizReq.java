package com.example.quiz11.vo;

public class GetQuizReq {
	private int quizId;

	public GetQuizReq() {
		super();
	}

	public GetQuizReq(int quizId) {
		super();
		this.quizId = quizId;
	}

	public int getQuizId() {
		return quizId;
	}

}
