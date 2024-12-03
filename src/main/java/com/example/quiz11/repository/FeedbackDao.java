package com.example.quiz11.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz11.entity.Feedback;
import com.example.quiz11.entity.FeedbackId;
import com.example.quiz11.vo.FeedbackDto;

@Repository
public interface FeedbackDao extends JpaRepository<Feedback, FeedbackId> {

	public boolean existsByQuizIdAndEmail(int quizId, String email);

	// 取同一張問卷所有人填答
	@Query(value = "SELECT new com.example.quiz11.vo.FeedbackDto(" //
			+ " qz.id, f.fillinDate, qz.name, qz.description," //
			+ " f.userName, f.phone, f.email, f.age," //
			+ " qu.quesId, qu.quesName, f.answer)" //
			+ " FROM Quiz qz" //
			+ " JOIN Ques qu ON qz.id = qu.quizId" //
			+ " JOIN Feedback f ON qz.id = f.quizId AND qu.quesId = f.quesId" //
			+ " WHERE qz.id = ?1", nativeQuery = false)
	public List<FeedbackDto> getFeedbackByQuizId(int quizId);

}
