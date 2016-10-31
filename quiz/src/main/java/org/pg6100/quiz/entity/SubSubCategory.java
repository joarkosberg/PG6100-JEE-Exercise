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
public class SubSubCategory {
    public static final String GET_SUB_SUB_CATEGORIES = "GET_SUB_SUB_CATEGORIES";

    @Id
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subSubCategory")
    private List<Question> questions;

    @ManyToOne
    private SubCategory subCategory;

    public SubSubCategory(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        if(questions == null){
            return new ArrayList<>();
        }
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }
}
