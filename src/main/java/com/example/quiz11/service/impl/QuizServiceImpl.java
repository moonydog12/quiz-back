package com.example.quiz11.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.example.quiz11.constants.QuesType;
import com.example.quiz11.constants.ResMessage;
import com.example.quiz11.entity.Ques;
import com.example.quiz11.entity.Quiz;
import com.example.quiz11.repository.QuesDao;
import com.example.quiz11.repository.QuizDao;
import com.example.quiz11.service.ifs.QuizService;
import com.example.quiz11.vo.BasicRes;
import com.example.quiz11.vo.CreateUpdateReq;

public class QuizServiceImpl implements QuizService {
	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuesDao quesDao;

	@Override
	public BasicRes create(CreateUpdateReq req) {
		// 參數檢查
		BasicRes checkResult = checkParams(req);
		if (checkResult != null) {
			return checkResult;
		}

		// 因為 quiz 的 pk 是流水號，不會重複寫入，所以不用檢查是否已存在相同 value
		// 因為 Quiz 中的 id 是 AI 自動生成的流水號，要讓 quizDao 執行 save 後可以把該 id 的值回傳，
		// 必須要在 Quiz 此 Entity 中將資料型態為 int 的屬性 id
		// 加上 @GeneratedValue(strategy = GenerationType.IDENTITY)
		// 新增問卷
		Quiz quizRes = quizDao.save(new Quiz(req.getName(), req.getDescription(), //
				req.getStartDate(), req.getEndDate(), req.isPublished()));
		// 將 quiz 中的 id 加入到 Ques
		int quizId = quizRes.getId();

		for (Ques item : req.getQuesList()) {
			item.setQuizId(quizId);
		}

		// 新增問題
		quesDao.saveAll(req.getQuesList());
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());

	}

	private BasicRes checkParams(CreateUpdateReq req) {
		// 檢查新增問卷時，id要為0
		if (req.getId() != 0) {
			return new BasicRes(ResMessage.QUIZ_PARAM_ERROR.getCode(), ResMessage.QUIZ_PARAM_ERROR.getMessage());
		}

		if (!StringUtils.hasText(req.getName()) || !StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.QUIZ_PARAM_ERROR.getCode(), ResMessage.QUIZ_PARAM_ERROR.getMessage());
		}

		// 檢查開始時間不得比結束時間晚
		if (req.getStartDate() == null || req.getEndDate() == null //
				|| req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.DATE_ERROR.getCode(), ResMessage.DATE_ERROR.getMessage());
		}

		// 檢查開始時間不能比今天早(問卷的開始時間最晚只能是今天)
		if (req.getStartDate().isBefore(LocalDate.now())) {
			return new BasicRes(ResMessage.DATE_ERROR.getCode(), ResMessage.DATE_ERROR.getMessage());
		}

		// 檢查Ques
		for (Ques item : req.getQuesList()) {
			if (item.getQuesId() <= 0 //
					|| !StringUtils.hasText(item.getType()) //
					|| !StringUtils.hasText(item.getQuesName()) //

			) {
				return new BasicRes(ResMessage.QUES_PARAM_ERROR.getCode(), ResMessage.QUES_PARAM_ERROR.getMessage());
			}
			// 檢查題目類型:單選(single)、多選(multi)、文字(text)
			if (!QuesType.checkType(item.getType())) {
				return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), ResMessage.QUES_TYPE_ERROR.getMessage());
			}

			// 檢查非文字類型時選項沒有值
			if (!item.getType().equalsIgnoreCase(QuesType.TEXT.toString()) && //
					!StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), ResMessage.QUES_TYPE_ERROR.getMessage());
			}
		}

		return null;
	}
}
