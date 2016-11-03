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
public class SubCategory {
    public static final String GET_SUB_CATEGORIES = "GET_SUB_CATEGORIES";

    @Id
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subCategory")
    private List<SubSubCategory> subSubCategories;

    @ManyToOne
    private Category category;

    public SubCategory(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubSubCategory> getSubSubCategories() {
        if(subSubCategories == null){
            return new ArrayList<>();
        }
        return subSubCategories;
    }

    public void setSubSubCategories(List<SubSubCategory> subSubCategories) {
        this.subSubCategories = subSubCategories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
