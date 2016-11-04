package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.entity.Category;

@ApiModel("A sub category belonging to a parent category")
public class SubCategoryDto {

    @ApiModelProperty("Name of subcategory")
    public String name;

    @ApiModelProperty("Parent category")
    public Category category;

    public SubCategoryDto(){}

    public SubCategoryDto(String name, Category category) {
        this.name = name;
        this.category = category;
    }
}