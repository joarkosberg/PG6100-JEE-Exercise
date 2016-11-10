package org.pg6100.quiz.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = Category.GET_ALL_CATEGORIES, query =
                "select c " +
                        "from Category c " +
                        "where TYPE(c) = Category")
})

@Entity
public class Category {
    public static final String GET_ALL_CATEGORIES= "GET_ALL_CATEGORIES";

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    public Category(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
