package com.example.consultationapp.service;

import com.example.consultationapp.domain.Answer;

public interface AnswerService {
    Answer save(Answer answer, Long questionId);
}
