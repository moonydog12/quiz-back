package com.example.quiz11.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quiz11.entity.Ques;
import com.example.quiz11.entity.QuesId;

@Repository
public interface QuesDao extends JpaRepository<Ques, QuesId> {

}
