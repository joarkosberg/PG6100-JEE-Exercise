package org.pg6100.quiz.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = SubCategory.GET_SUB_CATEGORIES, query =
                "select c " +
                        "from SubCategory c " +
                        "where c.category.name = :category")
})

@Entity
public class SubCategory extends Category{
    public static final String GET_SUB_CATEGORIES = "GET_SUB_CATEGORIES";

    @ManyToOne
    private Category category;

    public SubCategory(){
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
