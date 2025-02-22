package com.example.consultationapp.repository;

import com.example.consultationapp.domain.Keyword;
import com.example.consultationapp.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q from Question q join q.keywords k where k.word in :keywords")
    List<Question> findAllByKeywordsIsContaining(@Param("keywords") List<String> keywords);
}
