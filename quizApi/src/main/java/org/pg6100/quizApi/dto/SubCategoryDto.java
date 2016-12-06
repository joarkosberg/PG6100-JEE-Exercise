package org.pg6100.quizApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("A sub category belonging to a parent category")
public class SubCategoryDto {

    @ApiModelProperty("Id of the category")
    public String id;

    @ApiModelProperty("Name of subcategory")
    public String name;

    @ApiModelProperty("Parent category")
    public CategoryDto category;

    @ApiModelProperty("A list sub sub categories")
    public List<SubSubCategoryDto> subSubCategories;


    public SubCategoryDto(){}

    public SubCategoryDto(String id, String name, CategoryDto category, List<SubSubCategoryDto> subSubCategories) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subSubCategories = subSubCategories;
    }
}
