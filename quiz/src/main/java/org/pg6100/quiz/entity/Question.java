package org.pg6100.quiz.entity;

import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Question.GET_QUESTIONS, query =
                "select q " +
                        "from Question q " +
                        "where q.subSubCategory.name = :category")
})

@Entity
public class Question {
    public static final String GET_QUESTIONS = "GET_QUESTIONS";

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String question;

    @ElementCollection
    private List<String> answers;

    @NotNull
    private Integer correctAnswer;

    @ManyToOne
    private SubSubCategory subSubCategory;

    public Question(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public SubSubCategory getSubSubCategory() {
        return subSubCategory;
    }

    public void setSubSubCategory(SubSubCategory subSubCategory) {
        this.subSubCategory = subSubCategory;
    }
}