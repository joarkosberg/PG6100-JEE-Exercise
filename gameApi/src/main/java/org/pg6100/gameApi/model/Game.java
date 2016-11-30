package org.pg6100.gameApi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Game {

    @NotNull
    @JsonProperty
    private Long id;

    @NotNull
    @JsonProperty
    private Long[] questions;

    @NotNull
    @JsonProperty
    private int answeredQuestions;

    @NotNull
    @JsonProperty
    private String currentQuestion;

    public Game() {
    }

    public Game(Long id, Long[] questions, int answeredQuestions, String currentQuestion) {
        this.id = id;
        this.questions = questions;
        this.answeredQuestions = answeredQuestions;
        this.currentQuestion = currentQuestion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long[] getQuestions() {
        return questions;
    }

    public void setQuestions(Long[] questions) {
        this.questions = questions;
    }

    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(int answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(String currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}
