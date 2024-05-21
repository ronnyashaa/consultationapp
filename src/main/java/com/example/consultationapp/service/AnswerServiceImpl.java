package com.example.consultationapp.service;

import com.example.consultationapp.domain.Answer;
import com.example.consultationapp.repository.AnswerRepository;
import com.example.consultationapp.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService{

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public Answer save(Answer answer, Long questionId) {
        return questionRepository.findById(questionId)
                .map(question -> {question.addAnswer(answer);
                    questionRepository.save(question);
                    return answerRepository.save(answer);})
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id " + questionId));
    }
}
