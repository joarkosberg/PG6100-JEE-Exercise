package org.pg6100.quiz.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Category.GET_ALL_CATEGORIES, query =
                "select c " +
                        "from Category c"),
        @NamedQuery(name = Category.GET_CATEGORY, query =
                "select c " +
                        "from Category c " +
                        "where c.id = :id")
})

@Entity
public class Category extends BaseCategory{
    public static final String GET_ALL_CATEGORIES= "GET_ALL_CATEGORIES";
    public static final String GET_CATEGORY= "GET_CATEGORY";

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "category",
            cascade = CascadeType.ALL
    )
    private List<SubCategory> subCategories;

    public Category(){
        subCategories = new ArrayList<>();
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
