package org.pg6100.restApi.dto;

import org.pg6100.quiz.entity.SubSubCategory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubSubCategoryConverter {

    private SubSubCategoryConverter(){}

    public static SubSubCategoryDto transform(SubSubCategory entity){
        Objects.requireNonNull(entity);
        SubSubCategoryDto dto = new SubSubCategoryDto();
        dto.name = entity.getName();
        dto.subCategory = entity.getSubCategory();
        return dto;
    }

    public static List<SubSubCategoryDto> transform(List<SubSubCategory> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(SubSubCategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
