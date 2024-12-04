package com.example.quiz11.vo;

public class StatisticsDto {
	private String quizName;

	private int quesId;

	private String quesName;

	private String type;

	private String optionsStr;

	private String answerStr;

	public StatisticsDto() {
		super();
	}

	public StatisticsDto(String quizName, int quesId, String quesName, String type, String optionsStr,
			String answerStr) {
		super();
		this.quizName = quizName;
		this.quesId = quesId;
		this.quesName = quesName;
		this.type = type;
		this.optionsStr = optionsStr;
		this.answerStr = answerStr;
	}

	public String getQuizName() {
		return quizName;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public String getOptionsStr() {
		return optionsStr;
	}

	public String getAnswerStr() {
		return answerStr;
	}

	public String getType() {
		return type;
	}

}
