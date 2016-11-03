package org.pg6100.quiz.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = SubSubCategory.GET_SUB_SUB_CATEGORIES, query =
                "select c " +
                        "from SubSubCategory c " +
                        "where c.subCategory.name = :subCategory")
})

@Entity
public class SubSubCategory extends Category {
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
