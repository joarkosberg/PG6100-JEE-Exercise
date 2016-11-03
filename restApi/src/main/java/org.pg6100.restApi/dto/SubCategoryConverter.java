package org.pg6100.restApi.dto;

import org.pg6100.quiz.entity.SubCategory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubCategoryConverter {

    private SubCategoryConverter(){}

    public static SubCategoryDto transform(SubCategory entity){
        Objects.requireNonNull(entity);
        SubCategoryDto dto = new SubCategoryDto();
        dto.name = entity.getName();
        dto.category = entity.getCategory();
        return dto;
    }

    public static List<SubCategoryDto> transform(List<SubCategory> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(SubCategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
