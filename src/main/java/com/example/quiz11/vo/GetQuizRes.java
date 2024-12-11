package com.example.quiz11.vo;

import java.util.List;

import com.example.quiz11.entity.Ques;
import com.example.quiz11.entity.Quiz;

public class GetQuizRes extends BasicRes {
	private Quiz quiz;

	private List<Ques> quesList;

	public GetQuizRes() {
		super();
	}

	public GetQuizRes(int code, String message) {
		super(code, message);
	}

	public GetQuizRes(int code, String message, Quiz quiz, List<Ques> quesList) {
		super(code, message);
		this.quiz = quiz;
		this.quesList = quesList;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public List<Ques> getQues() {
		return quesList;
	}

}
