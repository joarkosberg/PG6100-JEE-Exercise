package org.pg6100.quizApi.dto.converter;

import org.pg6100.quiz.entity.Question;
import org.pg6100.quizApi.dto.QuestionDto;
import org.pg6100.quizApi.dto.QuestionDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuestionConverter {

    private QuestionConverter(){}

    public static QuestionDto transform(Question entity){
        Objects.requireNonNull(entity);
        QuestionDto dto = new QuestionDto();
        dto.id = String.valueOf(entity.getId());
        dto.question = entity.getQuestion();
        dto.answers = entity.getAnswers();
        dto.correctAnswer = entity.getCorrectAnswer();
        dto.subSubCategory = SubSubCategoryConverter.transform(entity.getSubSubCategory());
        return dto;
    }

    public static List<QuestionDto> transform(List<Question> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(QuestionConverter::transform)
                .collect(Collectors.toList());
    }
}
