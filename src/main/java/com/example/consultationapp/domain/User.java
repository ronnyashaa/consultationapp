package com.example.consultationapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public void addQuestion(Question question) {
        questions.add(question);
        question.setUser(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setUser(null);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setUser(this);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setUser(null);
    }
}
