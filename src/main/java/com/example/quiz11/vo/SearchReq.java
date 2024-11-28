package com.example.quiz11.vo;

import java.time.LocalDate;

public class SearchReq {
	private String name;

	private LocalDate StartDate;

	private LocalDate EndDate;

	public SearchReq() {
		super();
	}

	public SearchReq(String name, LocalDate startDate, LocalDate endDate) {
		super();
		this.name = name;
		StartDate = startDate;
		EndDate = endDate;
	}

	public String getName() {
		return name;
	}

	public LocalDate getStartDate() {
		return StartDate;
	}

	public LocalDate getEndDate() {
		return EndDate;
	}

}
