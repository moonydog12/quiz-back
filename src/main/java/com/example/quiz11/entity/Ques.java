package com.example.quiz11.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "ques")
@IdClass(value = QuesId.class)
public class Ques {
	@Id
	@Column(name = "quiz_id")
	private int quizId;

	@Id
	@Column(name = "ques_id")
	private int quesId;

	@Column(name = "ques_name")
	private String quesName;

	@Column(name = "type")
	private String type;

	@Column(name = "required")
	private boolean required;

	@Column(name = "options")
	private String options;

	public Ques() {
		super();
	}

	public Ques(int quizId, int quesId, String quesName, String type, boolean required, String options) {
		super();
		this.quizId = quizId;
		this.quesId = quesId;
		this.quesName = quesName;
		this.type = type;
		this.required = required;
		this.options = options;
	}

	public int getQuizId() {
		return quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public String getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

	public String getOptions() {
		return options;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

}
