package com.example.quiz11.vo;

public class Options {
	private int optionNumber;

	private String option;

	public Options() {
		super();
	}

	public Options(int optionNumber, String option) {
		super();
		this.optionNumber = optionNumber;
		this.option = option;
	}

	public int getOptionNumber() {
		return optionNumber;
	}

	public String getOption() {
		return option;
	}

}
