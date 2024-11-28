package com.example.quiz11.service.ifs;

import java.util.List;

import com.example.quiz11.vo.BasicRes;
import com.example.quiz11.vo.CreateUpdateReq;
import com.example.quiz11.vo.DeleteReq;

public interface QuizService {
	public BasicRes create(CreateUpdateReq req);

	public BasicRes update(CreateUpdateReq req);

	public BasicRes delete(DeleteReq req);
}
