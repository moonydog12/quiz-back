package com.example.quiz11.vo;

import java.util.List;

public class StatisticsRes extends BasicRes {
	private List<StatisticsVo> statisticsVolist;

	public StatisticsRes() {
		super();
	}

	public StatisticsRes(int code, String message) {
		super(code, message);
	}

	public StatisticsRes(int code, String message, List<StatisticsVo> statisticsVolist) {
		super(code, message);
		this.statisticsVolist = statisticsVolist;
	}

	public List<StatisticsVo> getStatisticsVolist() {
		return statisticsVolist;
	}

}
