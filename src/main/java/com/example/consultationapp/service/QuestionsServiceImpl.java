package com.example.consultationapp.service;

import com.example.consultationapp.domain.Answer;
import com.example.consultationapp.domain.Keyword;
import com.example.consultationapp.domain.Question;
import com.example.consultationapp.repository.AnswerRepository;
import com.example.consultationapp.repository.KeywordRepository;
import com.example.consultationapp.repository.QuestionRepository;
import com.example.consultationapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionsServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    @Transactional
    public List<Question> findAllByKeywords(List<String> keywords) {
        return questionRepository.findAllByKeywordsIsContaining(
                keywords.stream()
                        .map(String::toUpperCase)
                        .map(String::trim)
                        .collect(Collectors.toList()));
    }

    @Override
    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question with id %s not found".formatted(id)));
    }

    @Override
    public Question save(Question question, List<String> keywords) {
        keywords.stream()
                .map(String::toUpperCase)
                .map(String::trim)
                .forEach(keyword -> question.addKeyword(keywordRepository.findByWord(keyword)
                        .orElseGet(() -> keywordRepository.save(new Keyword(keyword)))));
        return questionRepository.save(question);
    }

    @Override
    public Question update(Long id, Question question, List<String> keywords) {
        Question updated = findById(id);
        updated.setTheme(question.getTheme());
        updated.setText(question.getText());
        updated.getKeywords().clear();
        keywords.stream()
                .map(String::toUpperCase)
                .map(String::trim)
                .forEach(keyword -> updated.addKeyword(keywordRepository.findByWord(keyword)
                        .orElseGet(() -> keywordRepository.save(new Keyword(keyword)))));
        return questionRepository.save(updated);
    }

    @Override
    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void switchIsAnswered(Long id) {
        var question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id " + id));
        question.setIsAnswered(!question.getIsAnswered());
    }

    @Override
    @Transactional
    public void addAnswer(String text, Long questionId, String username) {
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id " + questionId));
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username " + username));
        var answer = new Answer(text);
        user.addAnswer(answer);
        question.addAnswer(answer);
    }

    @Override
    @Transactional
    public void removeAnswer(Long answerId, Long questionId, String username) {
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id " + questionId));
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username " + username));
        var answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with id " + answerId));
        user.removeAnswer(answer);
        question.removeAnswer(answer);
        answerRepository.deleteById(answerId);
    }
}
