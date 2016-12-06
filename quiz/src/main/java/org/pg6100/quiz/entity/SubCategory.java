package org.pg6100.quiz.entity;

import javax.persistence.*;
import java.util.List;

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
public class SubCategory extends BaseCategory{
    public static final String GET_ALL_SUB_CATEGORIES = "GET_ALL_SUB_CATEGORIES";
    public static final String GET_SUB_CATEGORIES = "GET_SUB_CATEGORIES";

    @ManyToOne
    private Category category;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "subCategory",
            cascade = CascadeType.ALL
    )
    private List<SubSubCategory> subSubCategories;

    public SubCategory(){
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<SubSubCategory> getSubSubCategories() {
        return subSubCategories;
    }

    public void setSubSubCategories(List<SubSubCategory> subSubCategories) {
        this.subSubCategories = subSubCategories;
    }
}
