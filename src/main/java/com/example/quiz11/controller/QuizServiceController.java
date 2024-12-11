package com.example.quiz11.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

	@PostMapping(value = "quiz/delete")
	public BasicRes delete(@RequestBody DeleteReq req) {
		return quizService.delete(req);
	}

	@PostMapping(value = "quiz/search")
	public SearchRes search(@RequestBody SearchReq req) {
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
