package org.pg6100.quizApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("A Category for quizzes")
public class CategoryDto {

    @ApiModelProperty("Id of the category")
    public String id;

    @ApiModelProperty("Name of the category")
    public String name;

    @ApiModelProperty("A list sub categories")
    public List<SubCategoryDto> subCategories;

    public CategoryDto(){}

    public CategoryDto(String id, String name, List<SubCategoryDto> subCategories) {
        this.id = id;
        this.name = name;
        this.subCategories = subCategories;
    }
}
