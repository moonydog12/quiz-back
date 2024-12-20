package com.example.quiz11.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.quiz11.constants.QuesType;
import com.example.quiz11.constants.ResMessage;
import com.example.quiz11.entity.Feedback;
import com.example.quiz11.entity.Ques;
import com.example.quiz11.entity.Quiz;
import com.example.quiz11.repository.FeedbackDao;
import com.example.quiz11.repository.QuesDao;
import com.example.quiz11.repository.QuizDao;
import com.example.quiz11.service.ifs.QuizService;
import com.example.quiz11.vo.BasicRes;
import com.example.quiz11.vo.CreateUpdateReq;
import com.example.quiz11.vo.DeleteReq;
import com.example.quiz11.vo.FeedbackDto;
import com.example.quiz11.vo.FeedbackRes;
import com.example.quiz11.vo.FillinReq;
import com.example.quiz11.vo.GetQuizRes;
import com.example.quiz11.vo.Options;
import com.example.quiz11.vo.SearchReq;
import com.example.quiz11.vo.SearchRes;
import com.example.quiz11.vo.StatisticsDto;
import com.example.quiz11.vo.StatisticsRes;
import com.example.quiz11.vo.StatisticsVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Hidden;

@Service
public class QuizServiceImpl implements QuizService {
	// 常用工具寫成全域變數
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuesDao quesDao;

	@Autowired
	private FeedbackDao feedbackDao;

	// 因為同時操作兩個dao，所以要加上 Transactional，允許資料庫 roll back(全部成功或全部失敗
	@CacheEvict(cacheNames = "quiz_search")
	@Transactional
	@Override
	public BasicRes create(CreateUpdateReq req) {
		// 參數檢查
		// 檢查新增問卷時，id要為0
		if (req.getId() != 0) {
			return new BasicRes(ResMessage.QUIZ_PARAM_ERROR.getCode(), ResMessage.QUIZ_PARAM_ERROR.getMessage());
		}
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

		// 成功
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());

	}

	private BasicRes checkParams(CreateUpdateReq req) {

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

	// 更新
	@CacheEvict(cacheNames = "quiz_search")
	@Transactional
	@Override
	public BasicRes update(CreateUpdateReq req) {
		// 參數檢查
		// 檢查更新問卷時，因為問卷已存在資料庫中，所以 id 不能為 0
		if (req.getId() == 0) {
			return new BasicRes(ResMessage.QUIZ_PARAM_ERROR.getCode(), ResMessage.QUIZ_PARAM_ERROR.getMessage());
		}
		BasicRes checkResult = checkParams(req);
		if (checkResult != null) {
			return checkResult;
		}

		// 檢查 Ques list 中的 quiz_id 是否與 Quiz 的 id 相等
		int quizId = req.getId();

		for (Ques item : req.getQuesList()) {
			if (item.getQuizId() != quizId) {
				return new BasicRes(ResMessage.QUIZID_MISSMATCH.getCode(), //
						ResMessage.QUIZID_MISSMATCH.getMessage());
			}
		}

		// 問卷可以更新的狀態 1.未發布; 2.已發布但尚未開始
		Optional<Quiz> op = quizDao.findById(quizId);

		// 確認問卷是否存在
		if (op.isEmpty()) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}

		// 取得問卷(資料庫中的資料)
		Quiz quiz = op.get();

		// 確認問卷是否可以更新
		// 尚未發布: !quiz.isPublished()
		// 小括號分組用，但這裡可省略，因為 && 優先於 ||
		// 已發布但尚未開始: quiz.isPublished() && req.getStartDate().isAfter(LocalDate.now())
		if (!(!quiz.isPublished() || //
				(quiz.isPublished() && req.getStartDate().isAfter(LocalDate.now())))) {
			return new BasicRes(ResMessage.QUIZ_UPDATE_FAILED.getCode(), //
					ResMessage.QUIZ_UPDATE_FAILED.getMessage());
		}

		// 將 req 中的值 set 回從資料庫取出的 quiz 中: id 不需要 set
		quiz.setName(req.getName());
		quiz.setDescription(req.getDescription());
		quiz.setStartDate(req.getStartDate());
		quiz.setEndDate(req.getEndDate());
		quiz.setPublished(req.isPublished());

		// 更新問卷
		quizDao.save(quiz);

		// 先刪除相同 quiz_id 問卷中的所有問題，再重新新增
		quesDao.deleteByQuizId(quizId);
		quesDao.saveAll(req.getQuesList());

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	// 清除暫存資料: 只有 cacheNames 沒有 key，會把 cacheNames 是 quiz_search 的所有站存資料清除
	// 如果是 cacheNames + key 則是指清除特定的暫存資料
	// allEntries: 強調刪除指定的 cacheNames 底下所有 key 對應的站存資料，預設是 false
	@CacheEvict(cacheNames = "quiz_search", allEntries = true)
	@Override
	public BasicRes delete(DeleteReq req) {
		// 刪問卷
		quizDao.deleteByIdIn(req.getQuizIdList());

		// 刪相同 quiz_id 問卷的所有問題
		quesDao.deleteByQuizIdIn(req.getQuizIdList());

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	// cacheNmes 可以當成是書本目錄的"章"
	// cacheNames 等號後面字串可以多個，多個時要用大括號[]，如: chcheNames = {"A", "B"}
	// 因為 key 等號後面的字串中有多個字串要串接，所以必須要用 concat
	// Cache 不支援 cache ("字串1","字串2","字串3")寫法
	// concat 中非字串參數要使用方法 toString() 轉成字串
	// unless 可以翻成排除的意思，後面的字串是指會排除符合條件的 -> 排除 res 不成功，即只暫存成功時的資料
	// #result: 表示方法返回的結果: 即使是不同方法有不同的返回資料型態，也通用
	//	@Cacheable(cacheNames = "quiz_search", //
	//			key = "#req.name.concat('-').concat(#req.startDate.toString()).concat('-')" //
	//					+ ".concat(#req.endDate.toString())", //
	//			unless = "#result.code != 200")
	// for spring boot 3.x 版本
	// key 等號後面的字串，因為 req 是物件，使用 #req 會取不到參數值，簡單點的方法是使用位置 #p0 來表示方法中第一個參數
	// 多參數的串接，不使用 concat，直接在字串中使用加號(+)串多個參數
	// 字串中使用單引號來表示字串
	// 串接值的資料型態不是 String 時，可以使用 .toString() 轉換
	// #result: 表示方法返回的結果；即使是不同方法有不同的返回資料型態，也通用
	// unless 可以翻成排除的意思，後面的字串是指會排除符合條件的 --> 排除 res 不成功，即只暫存成功時的資料
	@Cacheable(cacheNames = "quiz_search", //
			key = "#p0.name + '-' + #p0.startDate.toString() + '-' + #p0.endDate.toString()", //
			unless = "#result.code != 200")

	@Override
	public SearchRes search(SearchReq req) {
		// 因為 service 中有使用 cache，所以必須要先確認 req 中參數的值都不是 null
		// 下方條件值得轉換放到 controller
//		// 檢視條件
//		String name = req.getName();
//
//		// 若 name = null 或空白字串， 一律轉成空字串
//		if (!StringUtils.hasText(name)) {
//			name = "";
//		}
//
//		String namePattern = "%" + name + "%";
//
//		LocalDate startDate = req.getStartDate();
//		// 若沒有開始日期條件，將日期轉成很早的時間
//		if (startDate == null) {
//			startDate = LocalDate.of(1970, 1, 1);
//		}
//
//		// 若沒有結束日期條件，將日期轉成長遠的未來時間
//		LocalDate endDate = req.getEndDate();
//		if (endDate == null) {
//			endDate = LocalDate.of(9999, 12, 31);
//
//		}

		List<Quiz> quizList = quizDao.getByCondition("%" + req.getName() + "%", req.getStartDate(), req.getEndDate());

		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), quizList);
	}

	@Override
	public BasicRes fillin(FillinReq req) {
		System.out.println("Answer Map: " + req.getAnswer());
		// 參數檢查
		if (req.getQuizId() <= 0) {
			return new BasicRes(ResMessage.QUIZ_ID_ERROR.getCode(), ResMessage.QUIZ_ID_ERROR.getMessage());
		}
		if (!StringUtils.hasText(req.getUserName()) || !StringUtils.hasText(req.getEmail())) {
			return new BasicRes(ResMessage.USERNAME_AND_EMAIL_REQUIRED.getCode(),
					ResMessage.USERNAME_AND_EMAIL_REQUIRED.getMessage());
		}

		if (CollectionUtils.isEmpty(req.getAnswer())) {
			return new BasicRes(ResMessage.ANSWER_REQUIRED.getCode(), ResMessage.ANSWER_REQUIRED.getMessage());
		}

		// 檢查同一張問卷是否重複填寫
		if (feedbackDao.existsByQuizIdAndEmail(req.getQuizId(), req.getEmail())) {
			return new BasicRes(ResMessage.EMAIL_DUPLICATED.getCode(), ResMessage.EMAIL_DUPLICATED.getMessage());
		}

		System.out.println(req.getAnswer()); // 比對資料庫的問卷和問題
		// 可以填寫的問卷必須是已經發布的
		Quiz quiz = quizDao.getByIdAndPublishedTrue(req.getQuizId());

		if (quiz == null) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}

		// 日期需要檢查填寫的日期是否是問卷可以填寫的時間範圍內
		if (req.getFillinDate() == null || req.getFillinDate().isBefore(quiz.getStartDate()) //
				|| req.getFillinDate().isAfter(quiz.getEndDate())) {
			return new BasicRes(ResMessage.DATE_RANGE_ERROR.getCode(), ResMessage.DATE_RANGE_ERROR.getMessage());
		}
		// 比對問題
		List<Ques> quesList = quesDao.getByQuizId(req.getQuizId());

		if (CollectionUtils.isEmpty(quesList)) {
			return new BasicRes(ResMessage.QUESTION_NOT_FOUND.getCode(), ResMessage.QUESTION_NOT_FOUND.getMessage());
		}

		// 題號 選項(1~n)
		Map<Integer, List<String>> answerMap = req.getAnswer();

		for (Ques item : quesList) {
			// req 中的選項(作答)
			List<String> ansList = answerMap.get(item.getQuesId());

			// 必填但沒有答案
			if (item.isRequired() && CollectionUtils.isEmpty(ansList)) {
				return new BasicRes(ResMessage.ANSWER_REQUIRED.getCode(), ResMessage.ANSWER_REQUIRED.getMessage());
			}

			// 單選和文字不能有多個答案
			if ((item.getType().equals(QuesType.SINGLE.getType()) //
					|| item.getType().equals(QuesType.TEXT.getType())) && ansList.size() > 1) {
				return new BasicRes(ResMessage.ONE_OPTION_IS_ALLOWED.getCode(), //
						ResMessage.ONE_OPTION_IS_ALLOWED.getMessage());
			}

			// 當問題 type 不是文字(簡答)類型時，需要將資料庫中的選項字串轉換成選項類別

			if (!item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				// 把 Ques 中的 options 字串轉成 Options 類別
				List<Options> optionsList = new ArrayList<>();

				try {
					optionsList = mapper.readValue(item.getOptions(), new TypeReference<>() {
					});
				} catch (Exception e) {
					return new BasicRes(ResMessage.OPTIONS_TRANSFER_ERROR.getCode(), //
							ResMessage.OPTIONS_TRANSFER_ERROR.getMessage());
				}

				// 蒐集 optionsList 中所有的 option
				List<String> optionsListInDB = new ArrayList<>();
				for (Options opt : optionsList) {
					optionsListInDB.add(opt.getOption());
				}

				// 比對 request 中的答案與資料庫中的選項是否一致
				// 因為 DB 中的選項會比答案選項多，所以用大的 list 去檢查是否包含小的 list 中的每一項
				for (String ans : ansList) {
					if (!optionsListInDB.contains(ans)) {
						return new BasicRes(ResMessage.OPTION_ANSWER_MISMATCH.getCode(), //
								ResMessage.OPTION_ANSWER_MISMATCH.getMessage());
					}
				}

			}

		}
		// 存資料
		List<Feedback> feedbackList = new ArrayList<>();

		// 題號的資料型態是 Integer: 答案選項的資料型態是 List<String>
		for (Entry<Integer, List<String>> map : answerMap.entrySet()) {
			// 將 List<String> 的答案轉成字串
			try {
				String str = mapper.writeValueAsString(map.getValue());
				Feedback feedback = new Feedback(req.getQuizId(), map.getKey(), str, req.getUserName(), req.getPhone(),
						req.getEmail(), req.getAge(), req.getFillinDate());
				feedbackList.add(feedback);
			} catch (JsonProcessingException e) {
				return new BasicRes(ResMessage.OPTIONS_TRANSFER_ERROR.getCode(), //
						ResMessage.OPTIONS_TRANSFER_ERROR.getMessage());

			}
		}

		feedbackDao.saveAll(feedbackList);

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	@Override
	public FeedbackRes feedback(int quizId) {
		// 參數檢查
		if (quizId <= 0) {
			return new FeedbackRes(ResMessage.QUIZ_ID_ERROR.getCode(), //
					ResMessage.QUIZ_ID_ERROR.getMessage());
		}

		List<FeedbackDto> list = feedbackDao.getFeedbackByQuizId(quizId);
		return new FeedbackRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

	@Override
	public StatisticsRes statistics(int quizId) {
		// 參數檢查
		if (quizId <= 0) {
			return new StatisticsRes(ResMessage.QUIZ_ID_ERROR.getCode(), ResMessage.QUIZ_ID_ERROR.getMessage());
		}

		List<StatisticsDto> dtoList = feedbackDao.getStatisticsByQuizId(quizId);
		if (dtoList.isEmpty()) {
			return new StatisticsRes(ResMessage.QUIZ_ID_ERROR.getCode(), ResMessage.QUIZ_ID_ERROR.getMessage(),
					new ArrayList<>());
		}

		// 用 Map 儲存每個問題的統計資料，避免重複計算
		Map<Integer, StatisticsVo> voMap = new HashMap<>();

		for (StatisticsDto dto : dtoList) {
			// 檢查並獲取對應的 StatisticsVo，若不存在則初始化
			StatisticsVo vo = voMap.computeIfAbsent(dto.getQuesId(),
					k -> new StatisticsVo(dto.getQuizName(), dto.getQuesId(), dto.getQuesName(), new HashMap<>()));

			// 如果是簡答題型，跳過該題
			if (dto.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				continue;
			}

			// 解析選項和答案
			List<Options> optionsList = new ArrayList<>();
			List<String> answerList = new ArrayList<>();
			try {
				// 解析選項
				if (!dto.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
					optionsList = mapper.readValue(dto.getOptionsStr(), new TypeReference<List<Options>>() {
					});
				}
				// 解析答案
				if (StringUtils.hasText(dto.getAnswerStr())
						&& !dto.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
					answerList = mapper.readValue(dto.getAnswerStr(), new TypeReference<List<String>>() {
					});
				}
			} catch (Exception e) {
				return new StatisticsRes(ResMessage.OPTIONS_TRANSFER_ERROR.getCode(),
						ResMessage.OPTIONS_TRANSFER_ERROR.getMessage());
			}

			// 初始化選項計數 (只有第一次遇到這個問題才初始化)
			if (vo.getOptionCountMap().isEmpty()) {
				optionsList.forEach(option -> vo.getOptionCountMap().put(option.getOption(), 0));
			}

			// 更新選項計數
			answerList.forEach(answer -> vo.getOptionCountMap().merge(answer, 1, Integer::sum));
		}

		// 返回統計結果
		return new StatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),
				new ArrayList<>(voMap.values()));

	}

	@Override
	public GetQuizRes getQuizById(int quizId) {
		Quiz quiz = quizDao.getById(quizId);
		List<Ques> quesList = quesDao.getByQuizId(quizId);

		return new GetQuizRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), quiz, quesList);

	}
}
