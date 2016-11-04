package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.entity.SubSubCategory;

import java.util.List;

@ApiModel("A question belonging to a category")
public class QuestionDto {

    @ApiModelProperty("Id of the question")
    public String id;

    @ApiModelProperty("The question")
    public String question;

    @ApiModelProperty("Possible answers for the question")
    public List<String> answers;

    @ApiModelProperty("Correct answer, integer between 0-3")
    public Integer correctAnswer;

    @ApiModelProperty("Category which the question belong to")
    public SubSubCategory subSubCategory;

    public QuestionDto(){}

    public QuestionDto(String id, String question, List<String> answers, Integer correctAnswer, SubSubCategory subSubCategory) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.subSubCategory = subSubCategory;
    }
}