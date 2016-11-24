package org.pg6100.quizApi.dto.converter;

import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.quizApi.dto.SubCategoryDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubCategoryConverter {

    private SubCategoryConverter(){}

    public static SubCategoryDto transform(SubCategory entity){
        Objects.requireNonNull(entity);
        SubCategoryDto dto = new SubCategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.name = entity.getName();
        dto.category = CategoryConverter.transform(entity.getCategory());
        return dto;
    }

    public static List<SubCategoryDto> transform(List<SubCategory> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(SubCategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
