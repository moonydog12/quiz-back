package com.example.quiz11.vo;

import java.util.List;

public class DeleteReq {
	private List<Integer> quizIdList;

	public DeleteReq() {
		super();
	}

	public DeleteReq(List<Integer> quizIdList) {
		super();
		this.quizIdList = quizIdList;
	}

	public List<Integer> getQuizIdList() {
		return quizIdList;
	}

}
