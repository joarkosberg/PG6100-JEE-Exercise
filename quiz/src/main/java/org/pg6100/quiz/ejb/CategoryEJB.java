package org.pg6100.quiz.ejb;

import org.pg6100.quiz.entity.Category;
import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.quiz.entity.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class CategoryEJB {

    @PersistenceContext
    private EntityManager em;

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

    public Category getCategory(Long id){
        return em.find(Category.class, id);
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

    public SubSubCategory getSubSubCategory(Long id){
        return em.find(SubSubCategory.class, id);
    }

    //Universal
    public boolean isPresent(Long id){
        return em.find(Category.class, id) != null;
    }

    public boolean update(@NotNull Long id, @NotNull String name){
        Category c = em.find(Category.class, id);
        if(c == null)
            return false;

        c.setName(name);
        return true;
    }

    public boolean delete(@NotNull Long id){
        Category c = em.find(Category.class, id);
        if (c != null) {
            em.remove(c);
            return true;
        }
        return false;
    }
}
