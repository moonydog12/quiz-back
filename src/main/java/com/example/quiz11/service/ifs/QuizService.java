package com.example.quiz11.service.ifs;

import com.example.quiz11.vo.BasicRes;
import com.example.quiz11.vo.CreateUpdateReq;
import com.example.quiz11.vo.DeleteReq;
import com.example.quiz11.vo.FeedbackRes;
import com.example.quiz11.vo.FillinReq;
import com.example.quiz11.vo.SearchReq;
import com.example.quiz11.vo.SearchRes;
import com.example.quiz11.vo.StatisticsRes;

public interface QuizService {
	public BasicRes create(CreateUpdateReq req);

	public BasicRes update(CreateUpdateReq req);

	public BasicRes delete(DeleteReq req);

	public SearchRes search(SearchReq req);

	public BasicRes fillin(FillinReq req);

	public FeedbackRes feedback(int quizId);

	public StatisticsRes statistics(int quizId);
}
