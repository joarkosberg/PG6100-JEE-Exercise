package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.entity.SubCategory;

import java.util.List;

@ApiModel("A Category for quizzes")
public class CategoryDto {

    @ApiModelProperty("Name of the category")
    public String name;

    public CategoryDto(){}

    public CategoryDto(String name) {
        this.name = name;
    }
}
