package org.pg6100.quiz.entity;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = Category.GET_ALL_CATEGORIES, query =
                "select c " +
                        "from Category c")
})

@Entity
public class Category {
    public static final String GET_ALL_CATEGORIES= "GET_ALL_CATEGORIES";

    @Id
    private String name;

    public Category(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
