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
    private List<SubSubCategory> categories;

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

    public List<SubSubCategory> getCategories() {
        if(categories == null){
            return new ArrayList<>();
        }
        return categories;
    }

    public void setCategories(List<SubSubCategory> categories) {
        this.categories = categories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
