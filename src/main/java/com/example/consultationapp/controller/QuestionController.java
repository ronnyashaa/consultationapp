package com.example.consultationapp.controller;

import com.example.consultationapp.domain.Question;
import com.example.consultationapp.service.AnswerService;
import com.example.consultationapp.service.QuestionService;
import com.example.consultationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping
    public String findAll(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("questions", questionService.findAll());
        return "questions";
    }

    @GetMapping("/search")
    public String findAllByWord(@RequestParam("word") String word, Model model, Principal principal) {
        if (word == null || word.isBlank()) word = "";
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("questions", questionService.findAllByKeywords(Arrays.asList(word.split(", "))));
        return "questions";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("question", questionService.findById(id));
        return "question";
    }

    @GetMapping("/{id}/update-page")
    public String updateByIdPage(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionService.findById(id));
        return "question-update";
    }

    @PostMapping
    public String save(@ModelAttribute Question question,
                       @ModelAttribute("keyword") String keywords,
                       Principal principal) {
        if (question.getTheme().isBlank() || question.getText().isBlank()) {
            String message = "Введіть тему і текст питання";
            return "redirect:/questions" + "?error=" +
                    URLEncoder.encode(message, StandardCharsets.UTF_8);
        }
        question.setUser(userService.findByUsername(principal.getName()));
        questionService.save(question, List.of(keywords.split(",")));
        return "redirect:/questions";
    }

    @PostMapping("/{id}/answers")
    public String saveAnswer(@ModelAttribute("text") String text,
                             @PathVariable Long id,
                             Principal principal) {
        if (text.isBlank()) {
            String message = "Введіть текст відповіді";
            return "redirect:/questions/" + id + "?error=" +
                    URLEncoder.encode(message, StandardCharsets.UTF_8);
        }
        questionService.addAnswer(text, id, principal.getName());
        return "redirect:/questions/" + id;
    }

    @PostMapping("/{id}/answers/{answer-id}")
    public String deleteAnswer(@PathVariable Long id,
                               @PathVariable("answer-id") Long answerId,
                               Principal principal) {
        questionService.removeAnswer(answerId, id, principal.getName());
        return "redirect:/questions/" + id;
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("keyword") String keywords,
                         @ModelAttribute Question question) {
        if (question.getTheme().isBlank() || question.getText().isBlank()) {
            String message = "Введіть тему і текст питання";
            return "redirect:/questions" + "?error=" +
                    URLEncoder.encode(message, StandardCharsets.UTF_8);
        }
        questionService.update(id, question, List.of(keywords.split(",")));
        return "redirect:/questions/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        questionService.deleteById(id);
        return "redirect:/questions";
    }

    @PostMapping("/{id}/switch-is-answered")
    public String switchIsAnswered(@PathVariable Long id) {
        questionService.switchIsAnswered(id);
        return "redirect:/questions/" + id;
    }
}
