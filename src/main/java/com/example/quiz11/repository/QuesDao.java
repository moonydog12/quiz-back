package com.example.quiz11.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz11.entity.Ques;
import com.example.quiz11.entity.QuesId;

@Repository
public interface QuesDao extends JpaRepository<Ques, QuesId> {

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ques WHERE quiz_id = ?1", nativeQuery = true)
	public int deleteByQuizId(int quizId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ques WHERE quiz_id IN(?1)", nativeQuery = true)
	public void deleteByQuizIdIn(List<Integer> quizIdList);

	@Query(value = "SELECT quiz_id, ques_id, ques_name, type, required, options FROM ques WHERE quiz_id = ?1", nativeQuery = true)
	public List<Ques> getByQuizId(int quizId);
}
