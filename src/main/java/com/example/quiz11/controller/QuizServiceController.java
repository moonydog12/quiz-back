package com.example.quiz11.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz11.service.ifs.QuizService;
import com.example.quiz11.vo.BasicRes;
import com.example.quiz11.vo.CreateUpdateReq;
import com.example.quiz11.vo.DeleteReq;
import com.example.quiz11.vo.FeedbackRes;
import com.example.quiz11.vo.FillinReq;
import com.example.quiz11.vo.GetQuizReq;
import com.example.quiz11.vo.GetQuizRes;
import com.example.quiz11.vo.SearchReq;
import com.example.quiz11.vo.SearchRes;
import com.example.quiz11.vo.StatisticsRes;

import io.swagger.v3.oas.annotations.Hidden;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class QuizServiceController {
	@Autowired
	private QuizService quizService;

	@PostMapping(value = "quiz/create")
	public BasicRes create(@RequestBody CreateUpdateReq req) {
		return quizService.create(req);
	}

	@PostMapping(value = "quiz/update")
	public BasicRes update(@RequestBody CreateUpdateReq req) {
		return quizService.update(req);
	}

	@Hidden
	@PostMapping(value = "quiz/delete")
	public BasicRes delete(@RequestBody DeleteReq req) {
		return quizService.delete(req);
	}

	@PostMapping(value = "quiz/search")
	public SearchRes search(@RequestBody SearchReq req) {
		// 因為 service 中有使用 cache，所以必須要先確認 req 中參數的值都不是 null
		// 檢視條件
		String name = req.getName();

		// 若 name = null 或空白字串， 一律轉成空字串
		if (!StringUtils.hasText(name)) {
			name = "";
			// 把新的值 set 回 req
			req.setName(name);
		}

//		String namePattern = "%" + name + "%";

		LocalDate startDate = req.getStartDate();
		// 若沒有開始日期條件，將日期轉成很早的時間
		if (startDate == null) {
			startDate = LocalDate.of(1970, 1, 1);
			req.setStartDate(startDate);
		}

		// 若沒有結束日期條件，將日期轉成長遠的未來時間
		LocalDate endDate = req.getEndDate();
		if (endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
			req.setEndDate(endDate);

		}

		return quizService.search(req);
	}

	@PostMapping(value = "quiz/fillin")
	public BasicRes fillin(@RequestBody FillinReq req) {
		return quizService.fillin(req);
	}

	@GetMapping(value = "quiz/feedback")
	public FeedbackRes feedback(@RequestParam int quizId) {
		return quizService.feedback(quizId);
	}

	@GetMapping(value = "quiz/statistics")
	public StatisticsRes statistics(@RequestParam int quizId) {
		return quizService.statistics(quizId);
	}

	@PostMapping(value = "quiz/getone")
	public GetQuizRes getone(@RequestBody GetQuizReq req) {
		return quizService.getQuizById(req.getQuizId());
	}
}
