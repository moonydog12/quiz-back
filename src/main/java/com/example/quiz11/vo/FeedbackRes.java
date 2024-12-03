package com.example.quiz11.vo;

import java.util.List;

public class FeedbackRes extends BasicRes {

	private List<FeedbackDto> feedbackDtoList;

	public FeedbackRes() {
		super();
	}

	public FeedbackRes(int code, String message) {
		super(code, message);
	}

	public FeedbackRes(int code, String message, List<FeedbackDto> feedbackDtoList) {
		super(code, message);
		this.feedbackDtoList = feedbackDtoList;
	}

	public List<FeedbackDto> getFeedbackDtoList() {
		return feedbackDtoList;
	}

}
