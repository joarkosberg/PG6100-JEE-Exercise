package org.pg6100.quizApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.ejb.CategoryEJB;
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
    public SubCategoryDto subCategory;

    public SubSubCategoryDto(){}

    public SubSubCategoryDto(String id, String name, SubCategoryDto subCategory) {
        this.id = id;
        this.name = name;
        this.subCategory = subCategory;
    }
}
