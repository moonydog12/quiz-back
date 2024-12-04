package com.example.quiz11.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz11.entity.Quiz;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM quiz WHERE id IN(?1)", nativeQuery = true)
	public void deleteByIdIn(List<Integer> idList);

	@Query(value = "SELECT id, name, description, start_date, end_date, published " + "FROM quiz "
			+ "WHERE name LIKE ?1 AND start_date >= ?2 AND end_date <= ?3", nativeQuery = true)
	public List<Quiz> getByCondition(String name, LocalDate startDate, LocalDate endDate);

	// public Quiz findByIdAndPublishedTrue(int quizId); // JPA 語法，功能同下

	// 語法中的 published is true 也可寫成 published = true; null 也適用
	@Query(value = "SELECT id, name, description, start_date, end_date, published" //
			+ " FROM quiz WHERE id = ?1 AND published IS true", nativeQuery = true)
	public Quiz getByIdAndPublishedTrue(int quizId);

	@Query(value = "SELECT id, name, description, start_date, end_date, published" //
			+ " FROM quiz WHERE id = ?1 AND published IS true"
			+ " AND start_date <= ?2 AND end_date >= ?2", nativeQuery = true)
	public Quiz getByIdAndPublishedTrueBetween(int quizId, LocalDate fillinDate);
}
