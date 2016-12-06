package org.pg6100.quizApi.dto.converter;

import org.pg6100.quiz.entity.Category;
import org.pg6100.quizApi.dto.CategoryDto;
import org.pg6100.quizApi.dto.CategoryDto;
import org.pg6100.quizApi.dto.ListDto;
import org.pg6100.quizApi.dto.SubCategoryDto;

import java.util.ArrayList;
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

    public static CategoryDto transform(Category entity, boolean expand){
        Objects.requireNonNull(entity);
        CategoryDto dto = new CategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.name = entity.getName();

        if(expand){
            dto.subCategories = new ArrayList<>();
            entity.getSubCategories().stream()
                    .map(SubCategoryConverter::transform)
                    .forEach(n -> dto.subCategories.add(n));
        }

        return dto;
    }

    public static ListDto<CategoryDto> transform(List<Category> entities, int offset, int limit, boolean expand){

        List<CategoryDto> dtoList = null;
        if(entities != null){
            dtoList = entities.stream()
                    .skip(offset)
                    .limit(limit)
                    .map(n -> transform(n, expand))
                    .collect(Collectors.toList());
        }

        ListDto<CategoryDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = entities.size();

        return dto;
    }
}
