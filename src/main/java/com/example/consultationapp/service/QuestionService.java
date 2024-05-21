package com.example.consultationapp.service;

import com.example.consultationapp.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> findAll();

    List<Question> findAllByKeywords(List<String> keywords);

    Question findById(Long id);

    Question save(Question question, List<String> keywords);

    Question update(Long id, Question question, List<String> keywords);

    void deleteById(Long id);

    void switchIsAnswered(Long id);

    void addAnswer(String text, Long questionId, String username);

    void removeAnswer(Long answerId, Long questionId, String username);
}
