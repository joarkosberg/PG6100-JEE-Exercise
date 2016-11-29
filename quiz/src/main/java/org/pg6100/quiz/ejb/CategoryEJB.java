package org.pg6100.quiz.ejb;

import org.pg6100.quiz.entity.Category;
import org.pg6100.quiz.entity.Question;
import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.quiz.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Stateless
public class CategoryEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private QuestionEJB questionEJB;

    /*
    Category
     */
    public Long createNewCategory(@NotNull String name){
        Category category = new Category();
        category.setName(name);
        em.persist(category);

        return category.getId();
    }

    public List<Category> getAllCategories(){
        Query query = em.createNamedQuery(Category.GET_ALL_CATEGORIES);
        return query.getResultList();
    }

    public List<Category> getCategoriesWithQuestions(){
        List<Question> questions = questionEJB.getAllQuestions();
        if(questions.size() == 0){
            return new ArrayList<>();
        }

        Set<Long> categories = questions
                .stream()
                .map(q -> q.getSubSubCategory().getSubCategory().getCategory().getId())
                .collect(toSet());

        return getAllCategories()
                .stream()
                .filter(c -> categories.contains(c.getId()))
                .collect(Collectors.toList());
    }

    public Category getCategory(Long id){
        Query query = em.createNamedQuery(Category.GET_CATEGORY);
        query.setParameter("id", id);
        return (Category) query.getSingleResult();
    }

    public boolean isCategoryPresent(Long id){
        Query query = em.createNamedQuery(Category.GET_CATEGORY);
        query.setParameter("id", id);
        return query.getResultList().size() > 0;
    }

    public boolean updateCategory(@NotNull Long id, @NotNull String name){
        Category c = em.find(Category.class, id);
        if(c == null)
            return false;

        c.setName(name);
        return true;
    }

    /*
    Sub Category
     */
    public Long createNewSubCategory(@NotNull String name, @NotNull Long categoryId){
        Category category = em.find(Category.class, categoryId);
        if(category == null){
            return null;
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCategory(category);

        em.persist(subCategory);
        return subCategory.getId();
    }

    public List<SubCategory> getAllSubCategories(){
        Query query = em.createNamedQuery(SubCategory.GET_ALL_SUB_CATEGORIES);
        return query.getResultList();
    }

    public List<SubCategory> getSubCategories(Long categoryId){
        Query query = em.createNamedQuery(SubCategory.GET_SUB_CATEGORIES);
        query.setParameter("id", categoryId);
        return query.getResultList();
    }

    public SubCategory getSubCategory(Long id){
        return em.find(SubCategory.class, id);
    }

    public boolean isSubCategoryPresent(Long id){
        return em.find(SubCategory.class, id) != null;
    }

    public boolean updateSubCategory(@NotNull Long id, @NotNull String name, @NotNull Long categoryId){
        SubCategory c = em.find(SubCategory.class, id);
        if(c == null)
            return false;
        c.setName(name);
        c.setCategory(getCategory(categoryId));
        return true;
    }

    public boolean updateSubCategoryName(@NotNull Long id, @NotNull String name){
        SubCategory c = em.find(SubCategory.class, id);
        if(c == null)
            return false;
        c.setName(name);
        return true;
    }

    /*
    Sub Sub Category
     */
    public Long createNewSubSubCategory(@NotNull String name, Long subCategoryId){
        SubCategory subCategory = em.find(SubCategory.class, subCategoryId);
        if(subCategory == null){
            return null;
        }

        SubSubCategory subSubCategory = new SubSubCategory();
        subSubCategory.setName(name);
        subSubCategory.setSubCategory(subCategory);
        em.persist(subSubCategory);

        return subSubCategory.getId();
    }

    public List<SubSubCategory> getAllSubSubCategories(){
        Query query = em.createNamedQuery(SubSubCategory.GET_ALL_SUB_SUB_CATEGORIES);
        return query.getResultList();
    }

    public List<SubSubCategory> getSubSubCategories(Long subCategoryId){
        Query query = em.createNamedQuery(SubSubCategory.GET_SUB_SUB_CATEGORIES);
        query.setParameter("id", subCategoryId);
        return query.getResultList();
    }

    public List<SubSubCategory> getSubSubCategoriesWithQuestions(Integer n){
        List<Question> questions = questionEJB.getAllQuestions();
        if(questions.size() < n){
            return new ArrayList<>();
        }

        Map<Long, Integer> subSubWithQuestions = new HashMap<>();

        questions.forEach(q -> subSubWithQuestions
                .put(q.getSubSubCategory().getId(), 0));

        for(Question q: questions){
            int i = subSubWithQuestions.get(q.getSubSubCategory().getId());
            i++;
            subSubWithQuestions.put(q.getSubSubCategory().getId(), i);
        }

        List<Long> validIds = subSubWithQuestions.keySet().stream()
                .filter(c -> subSubWithQuestions.get(c) >= n)
                .collect(Collectors.toList());

        return getAllSubSubCategories()
                .stream()
                .filter(c -> validIds.contains(c.getId()))
                .collect(Collectors.toList());
    }

    public SubSubCategory getSubSubCategory(Long id){
        return em.find(SubSubCategory.class, id);
    }

    public boolean isSubSubCategoryPresent(Long id){
        return em.find(SubSubCategory.class, id) != null;
    }

    public boolean updateSubSubCategory(@NotNull Long id, @NotNull String name, @NotNull Long subCategoryId){
        SubSubCategory c = em.find(SubSubCategory.class, id);
        if(c == null)
            return false;

        c.setName(name);
        c.setSubCategory(getSubCategory(subCategoryId));

        return true;
    }

    //Universal
    public boolean delete(@NotNull Long id){
        Category c = em.find(Category.class, id);
        if (c != null) {
            em.remove(c);
            return true;
        }
        return false;
    }
}
