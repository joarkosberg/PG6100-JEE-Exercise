package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.quiz.entity.SubSubCategory;

import javax.ejb.EJB;

@ApiModel("Subcategory of the subcategory")
public class SubSubCategoryDto {

    @EJB
    private CategoryEJB categoryEJB;

    @ApiModelProperty("Id of the category")
    public String id;

    @ApiModelProperty("Name of the category")
    public String name;

    @ApiModelProperty("Parent category")
    public SubCategory subCategory;

    public SubSubCategoryDto(){}

    public SubSubCategoryDto(String id, String name, SubCategory subCategory) {
        this.id = id;
        this.name = name;
        this.subCategory = subCategory;
    }
}
