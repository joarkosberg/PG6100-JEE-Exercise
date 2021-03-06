package org.pg6100.quiz.entity;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = SubSubCategory.GET_ALL_SUB_SUB_CATEGORIES, query =
                "select c " +
                        "from SubSubCategory c"),
        @NamedQuery(name = SubSubCategory.GET_SUB_SUB_CATEGORIES, query =
                "select c " +
                        "from SubSubCategory c " +
                        "where c.subCategory.id = :id")
})

@Entity
public class SubSubCategory extends BaseCategory {
    public static final String GET_ALL_SUB_SUB_CATEGORIES = "GET_ALL_SUB_SUB_CATEGORIES";
    public static final String GET_SUB_SUB_CATEGORIES = "GET_SUB_SUB_CATEGORIES";

    @ManyToOne
    private SubCategory subCategory;

    public SubSubCategory(){
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }
}
