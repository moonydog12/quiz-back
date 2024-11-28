package com.example.quiz11.constants;

public enum ResMessage {

	SUCCESS(200, "success"), //
	QUIZ_PARAM_ERROR(400, "Quiz param error"), //
	DATE_ERROR(400, "Date error"), //
	QUES_PARAM_ERROR(400, "Ques param error"), //
	QUES_TYPE_ERROR(400, "Ques type error"), //
	QUIZID_MISSMATCH(400, "QuizId mismatch"), //
	QUIZ_NOT_FOUND(404, "Quiz not found"), //
	QUIZ_UPDATE_FAILED(400, "Quiz update failed");

	private int code;

	private String message;

	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
