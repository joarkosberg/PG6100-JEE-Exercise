package org.pg6100.quiz.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Category.GET_ALL_CATEGORIES, query =
                "select c " +
                        "from Category c"),
        @NamedQuery(name = Category.GET_ALL_CATEGORIES, query =
                "select c " +
                        "from Category c")
})

@Entity
public class Category {
    public static final String GET_ALL_CATEGORIES= "GET_ALL_CATEGORIES";
    public static final String GET_SUB_CATEGORIES = "GET_SUB_CATEGORIES";
    public static final String GET_COUNT_OF_POSTS_BY_COUNTRY = "GET_COUNT_OF_POSTS_BY_COUNTRY";
    public static final String GET_COUNT_OF_ALL_USERS = "GET_COUNT_OF_ALL_USERS";

    @Id
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private List<SubCategory> categories;

    public Category(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategory> getCategories() {
        if(categories == null){
            return new ArrayList<>();
        }
        return categories;
    }

    public void setCategories(List<SubCategory> categories) {
        this.categories = categories;
    }
}
