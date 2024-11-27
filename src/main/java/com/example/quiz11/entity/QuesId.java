package com.example.quiz11.entity;

import java.io.Serializable;

public class QuesId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int quizId;
	private int quesId;

	public QuesId() {
		super();
	}

	public QuesId(int quizId, int quesId) {
		super();
		this.quizId = quizId;
		this.quesId = quesId;
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

}
