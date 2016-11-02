package org.pg6100.quiz.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pg6100.quiz.entity.Category;
import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.quiz.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.pg6100.quiz")
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CategoryEJB categoryEJB;

    @Test
    public void testCreateCategories(){
        int size = categoryEJB.getAllCategories().size();
        String category = categoryEJB.createNewCategory("category1");
        categoryEJB.createNewCategory("category2");

        List<Category> categories = categoryEJB.getAllCategories();
        assertEquals(size + 2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> category.equals(c.getName())));
    }

    @Test
    public void testCreateSubCategory(){
        String category = categoryEJB.createNewCategory("category3");
        String subCategory = categoryEJB.createNewSubCategory("sub1", category);

        List<SubCategory> subCategories = categoryEJB.getSubCategories(category);
        assertEquals(1, subCategories.size());
        assertTrue(subCategories.stream().anyMatch(c -> subCategory.equals(c.getName())));
    }

    @Test
    public void testCreateSubSubCategory(){
        String category = categoryEJB.createNewCategory("category4");
        String subCategory = categoryEJB.createNewSubCategory("sub2", category);
        String subSubCategory = categoryEJB.createNewSubSubCategory("subsub1", subCategory);

        List<SubSubCategory> subSubCategories = categoryEJB.getSubSubCategories(subCategory);
        assertEquals(1, subSubCategories.size());
        assertTrue(subSubCategories.stream().anyMatch(c -> subSubCategory.equals(c.getName())));
    }

    @Test
    public void testDifferentCategoriesWithDifferentSubs(){
        //Categories
        String category1 = categoryEJB.createNewCategory("c1");
        String category2 = categoryEJB.createNewCategory("c2");
        String category3 = categoryEJB.createNewCategory("c3");

        //Subcategories
        String subCategory1 = categoryEJB.createNewSubCategory("s1", category1);
        String subCategory2 = categoryEJB.createNewSubCategory("s2", category3);

        assertEquals(0, categoryEJB.getSubCategories(category2).size());
        assertEquals(1, categoryEJB.getSubCategories(category3).size());

        //SubSubcategories
        categoryEJB.createNewSubSubCategory("ss1", subCategory2);
        categoryEJB.createNewSubSubCategory("ss2", subCategory2);
        categoryEJB.createNewSubSubCategory("ss3", subCategory2);
        String subSubCategory4 = categoryEJB.createNewSubSubCategory("ss4", subCategory1);

        assertEquals(3, categoryEJB.getSubSubCategories(subCategory2).size());
        assertEquals(1, categoryEJB.getSubSubCategories(subCategory1).size());
        assertFalse(categoryEJB.getSubSubCategories(subCategory2).stream().
                anyMatch(c -> subSubCategory4.equals(c.getName())));
    }

    @Test(expected = EJBException.class)
    public void testCreationOfSubWithoutValidCategory(){
        categoryEJB.createNewSubCategory("NotSuchValid", "ThisNoWork");
    }
}
