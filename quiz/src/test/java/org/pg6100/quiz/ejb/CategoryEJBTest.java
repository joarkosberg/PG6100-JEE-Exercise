package org.pg6100.quiz.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pg6100.quiz.entity.Category;
import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.quiz.entity.SubSubCategory;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryEJBTest extends EJBTestBase {

    @Test
    public void testCreateCategories(){
        Long id = categoryEJB.createNewCategory("category1");
        categoryEJB.createNewCategory("category2");

        List<Category> categories = categoryEJB.getAllCategories();
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> id.equals(c.getId())));
    }

    @Test
    public void testCreateSubCategory(){
        Long id = categoryEJB.createNewCategory("category3");
        Long subId = categoryEJB.createNewSubCategory("sub1", id);

        List<SubCategory> subCategories = categoryEJB.getSubCategories(id);
        assertEquals(1, subCategories.size());
        assertTrue(subCategories.stream().anyMatch(c -> subId.equals(c.getId())));
    }

    @Test
    public void testCreateSubSubCategory(){
        Long category = categoryEJB.createNewCategory("category4");
        Long subCategory = categoryEJB.createNewSubCategory("sub2", category);
        Long subSubCategory = categoryEJB.createNewSubSubCategory("subsub1", subCategory);

        List<SubSubCategory> subSubCategories = categoryEJB.getSubSubCategories(subCategory);
        assertEquals(1, subSubCategories.size());
        assertTrue(subSubCategories.stream().anyMatch(c -> subSubCategory.equals(c.getId())));
    }

    @Test
    public void testDifferentCategoriesWithDifferentSubs(){
        //Categories
        Long category1 = categoryEJB.createNewCategory("c1");
        Long category2 = categoryEJB.createNewCategory("c2");
        Long category3 = categoryEJB.createNewCategory("c3");

        //Subcategories
        Long subCategory1 = categoryEJB.createNewSubCategory("s1", category1);
        Long subCategory2 = categoryEJB.createNewSubCategory("s2", category3);

        assertEquals(0, categoryEJB.getSubCategories(category2).size());
        assertEquals(1, categoryEJB.getSubCategories(category3).size());

        //SubSubcategories
        categoryEJB.createNewSubSubCategory("ss1", subCategory2);
        categoryEJB.createNewSubSubCategory("ss2", subCategory2);
        categoryEJB.createNewSubSubCategory("ss3", subCategory2);
        Long subSubCategory4 = categoryEJB.createNewSubSubCategory("ss4", subCategory1);

        assertEquals(3, categoryEJB.getAllCategories().size());
        assertEquals(3, categoryEJB.getSubSubCategories(subCategory2).size());
        assertEquals(1, categoryEJB.getSubSubCategories(subCategory1).size());
        assertFalse(categoryEJB.getSubSubCategories(subCategory2).stream().
                anyMatch(c -> subSubCategory4.equals(c.getId())));
    }

    @Test
    @Ignore //Deprecated
    public void testCreateTwoCategoriesWithSameName(){
        Long category1 = categoryEJB.createNewCategory("NoWork");
        Long category2 = categoryEJB.createNewCategory("NoWork");

        assertEquals(1, categoryEJB.getAllCategories().size());
        assertNotNull(category1);
        assertNull(category2);
    }

    @Test
    public void testCreationOfSubWithoutValidCategory(){
        assertNull(categoryEJB.createNewSubCategory("NotSuchValid", Long.valueOf(123)));
    }

    @Test
    public void testUpdateMethods(){
        Long category = categoryEJB.createNewCategory("name");
        Long subCategory = categoryEJB.createNewSubCategory("sub", category);
        Long subSubCategory = categoryEJB.createNewSubSubCategory("sub", subCategory);

        String newName = "new";
        assertTrue(categoryEJB.updateCategory(category, newName));
        assertEquals(newName, categoryEJB.getCategory(category).getName());

        assertTrue(categoryEJB.updateSubCategory(subCategory, newName, category));
        assertEquals(newName, categoryEJB.getSubCategory(subCategory).getName());

        assertTrue(categoryEJB.updateSubSubCategory(subSubCategory, newName, subCategory));
        assertEquals(newName, categoryEJB.getSubSubCategory(subSubCategory).getName());
    }

    @Test
    public void testGetCategoriesWithQuestions(){
        assertEquals(0, categoryEJB.getCategoriesWithQuestions().size());

        Long category1 = categoryEJB.createNewCategory("c1");
        Long category2 = categoryEJB.createNewCategory("c2");
        Long category3 = categoryEJB.createNewCategory("c3");

        Long subCategory1 = categoryEJB.createNewSubCategory("s1", category1);
        Long subCategory2 = categoryEJB.createNewSubCategory("s2", category3);
        Long subCategory3 = categoryEJB.createNewSubCategory("s2", category3);

        Long subSubCategory1 = categoryEJB.createNewSubSubCategory("ss1", subCategory1);
        Long subSubCategory2 = categoryEJB.createNewSubSubCategory("ss2", subCategory2);
        Long subSubCategory3 = categoryEJB.createNewSubSubCategory("ss3", subCategory3);

        assertEquals(0, categoryEJB.getCategoriesWithQuestions().size());

        questionEJB.createQuestion(subSubCategory1, "Question", answers, 3);
        questionEJB.createQuestion(subSubCategory2, "Question", answers, 3);
        questionEJB.createQuestion(subSubCategory3, "Question", answers, 3);

        List<Category> categories = categoryEJB.getCategoriesWithQuestions();
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals(category1)));
        assertFalse(categories.stream().anyMatch(c -> c.getId().equals(category2)));
    }

    @Test
    public void testGetSubSubCategoriesWithQuestions(){
        Long category1 = categoryEJB.createNewCategory("c1");
        Long subCategory1 = categoryEJB.createNewSubCategory("s1", category1);

        Long subSubCategory1 = categoryEJB.createNewSubSubCategory("ss1", subCategory1);
        Long subSubCategory2 = categoryEJB.createNewSubSubCategory("ss2", subCategory1);
        Long subSubCategory3 = categoryEJB.createNewSubSubCategory("ss3", subCategory1);

        questionEJB.createQuestion(subSubCategory1, "Question", answers, 3);
        questionEJB.createQuestion(subSubCategory3, "Question", answers, 3);
        questionEJB.createQuestion(subSubCategory1, "Question", answers, 3);

        List<SubSubCategory> subSubCategories = categoryEJB.getSubSubCategoriesWithQuestions(1);
        assertEquals(2, subSubCategories.size());
        assertTrue(subSubCategories.stream().anyMatch(c -> c.getId().equals(subSubCategory1)));
        assertFalse(subSubCategories.stream().anyMatch(c -> c.getId().equals(subSubCategory2)));
    }
}
