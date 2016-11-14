package org.pg6100.quiz.entity;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = SubCategory.GET_ALL_SUB_CATEGORIES, query =
                "select c " +
                        "from SubCategory c"),
        @NamedQuery(name = SubCategory.GET_SUB_CATEGORIES, query =
                "select c " +
                        "from SubCategory c " +
                        "where c.category.id = :id")
})

@Entity
public class SubCategory extends Category{
    public static final String GET_ALL_SUB_CATEGORIES = "GET_ALL_SUB_CATEGORIES";
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
