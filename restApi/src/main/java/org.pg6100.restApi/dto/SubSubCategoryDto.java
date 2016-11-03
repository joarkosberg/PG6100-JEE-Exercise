package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.entity.SubCategory;

@ApiModel("Subcategory of the subcategory")
public class SubSubCategoryDto {

    @ApiModelProperty("Name of the category")
    public String name;

    @ApiModelProperty("Parent category")
    public SubCategory subCategory;

    public SubSubCategoryDto(){}

    public SubSubCategoryDto(String name, SubCategory subCategory) {
        this.name = name;
        this.subCategory = subCategory;
    }
}
