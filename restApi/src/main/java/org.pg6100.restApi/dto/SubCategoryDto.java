package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A sub category belonging to a parent category")
public class SubCategoryDto {

    @ApiModelProperty("Id of the category")
    public String id;

    @ApiModelProperty("Name of subcategory")
    public String name;

    @ApiModelProperty("Parent category")
    public CategoryDto category;

    public SubCategoryDto(){}

    public SubCategoryDto(String id, String name, CategoryDto category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }
}
