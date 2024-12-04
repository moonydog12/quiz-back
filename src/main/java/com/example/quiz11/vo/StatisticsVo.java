package com.example.quiz11.vo;

import java.util.Map;

public class StatisticsVo {
	private String quizName;

	private int quesId;

	private String quesName;
	// 選項 次數
	private Map<String, Integer> optionCountMap;

	public StatisticsVo() {
		super();
	}

	public StatisticsVo(String quizName, int quesId, String quesName, Map<String, Integer> optionCountMap) {
		super();
		this.quizName = quizName;
		this.quesId = quesId;
		this.quesName = quesName;
		this.optionCountMap = optionCountMap;
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

	public Map<String, Integer> getOptionCountMap() {
		return optionCountMap;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public void setQuesName(String quesName) {
		this.quesName = quesName;
	}

	public void setOptionCountMap(Map<String, Integer> optionCountMap) {
		this.optionCountMap = optionCountMap;
	}

}
