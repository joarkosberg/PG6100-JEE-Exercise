package org.pg6100.quizApi.dto.converter;

import org.pg6100.quiz.entity.Category;
import org.pg6100.quizApi.dto.CategoryDto;
import org.pg6100.quizApi.dto.CategoryDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CategoryConverter {

    private CategoryConverter(){}

    public static CategoryDto transform(Category entity){
        Objects.requireNonNull(entity);
        CategoryDto dto = new CategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.name = entity.getName();
        return dto;
    }

    public static List<CategoryDto> transform(List<Category> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(CategoryConverter::transform)
                .collect(Collectors.toList());
    }
}