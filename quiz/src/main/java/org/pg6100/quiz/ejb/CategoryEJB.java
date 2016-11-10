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
    public String createNewCategory(@NotNull String name){
        if(em.find(Category.class, name) != null)
            return null;

        Category category = new Category();
        category.setName(name);
        em.persist(category);

        return category.getName();
    }

    public List<Category> getAllCategories(){
        Query query = em.createNamedQuery(Category.GET_ALL_CATEGORIES);
        return query.getResultList();
    }

    public Category getCategory(String name){
        return em.find(Category.class, name);
    }

    public boolean updateCategory(@NotNull String id, @NotNull String name){
        Category c = em.find(Category.class, id);
        if(c == null)
            return false;

        c.setName(name);
        return true;
    }

    /*
    Sub Category
     */
    public String createNewSubCategory(@NotNull String name, @NotNull String categoryName){
        Category category = em.find(Category.class, categoryName);
        if(category == null){
            return null;
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCategory(category);

        em.persist(subCategory);
        return subCategory.getName();
    }

    public List<SubCategory> getAllSubCategories(){
        Query query = em.createNamedQuery(SubCategory.GET_ALL_SUB_CATEGORIES);
        return query.getResultList();
    }

    public List<SubCategory> getSubCategories(String categoryName){
        Query query = em.createNamedQuery(SubCategory.GET_SUB_CATEGORIES);
        query.setParameter("category", categoryName);
        return query.getResultList();
    }

    public SubCategory getSubCategory(String name){
        return em.find(SubCategory.class, name);
    }

    /*
    Sub Sub Category
     */
    public String createNewSubSubCategory(@NotNull String name, String subCategoryName){
        SubCategory subCategory = em.find(SubCategory.class, subCategoryName);
        if(subCategory == null){
            return null;
        }

        SubSubCategory subSubCategory = new SubSubCategory();
        subSubCategory.setName(name);
        subSubCategory.setSubCategory(subCategory);

        em.persist(subSubCategory);

        return subSubCategory.getName();
    }

    public List<SubSubCategory> getAllSubSubCategories(){
        Query query = em.createNamedQuery(SubSubCategory.GET_ALL_SUB_SUB_CATEGORIES);
        return query.getResultList();
    }

    public List<SubSubCategory> getSubSubCategories(String subCategoryName){
        Query query = em.createNamedQuery(SubSubCategory.GET_SUB_SUB_CATEGORIES);
        query.setParameter("subCategory", subCategoryName);
        return query.getResultList();
    }

    public SubSubCategory getSubSubCategory(String name){
        return em.find(SubSubCategory.class, name);
    }

    //Universal
    public boolean isPresent(String name){
        return em.find(Category.class, name) != null;
    }

    public boolean deleteCategory(@NotNull String name){
        Category c = em.find(Category.class, name);
        if (c != null) {
            em.remove(c);
            return true;
        }
        return false;
    }
}
