package org.pg6100.quiz.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class QuestionEJBTest {
    private List<String> answers = Arrays.asList("a1", "a2", "a3", "a4");

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.pg6100.quiz")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CategoryEJB categoryEJB;
    @EJB
    private QuestionEJB questionEJB;

    @Before
    @After
    public void cleanDatabase() {
        questionEJB.getAllQuestions().stream().forEach(n -> questionEJB.deleteQuestion(n.getId()));
        assertEquals(0, questionEJB.getAllQuestions().size());
        categoryEJB.getAllSubSubCategories().stream().forEach(n -> categoryEJB.delete(n.getId()));
        assertEquals(0, categoryEJB.getAllSubSubCategories().size());
        categoryEJB.getAllSubCategories().stream().forEach(n -> categoryEJB.delete(n.getId()));
        assertEquals(0, categoryEJB.getAllSubCategories().size());
        categoryEJB.getAllCategories().stream().forEach(n -> categoryEJB.delete(n.getId()));
        assertEquals(0, categoryEJB.getAllCategories().size());
    }

    @Test
    public void testCreateQuestions(){
        Long category = categoryEJB.createNewCategory("category1");
        Long subCategory = categoryEJB.createNewSubCategory("sub", category);
        Long subSubCategory = categoryEJB.createNewSubSubCategory("subsub", subCategory);

        Long question = questionEJB.createQuestion(subSubCategory, "Question", answers, 3);
        assertNotNull(question);
        assertEquals(1, questionEJB.getQuestions(subSubCategory).size());
    }

    @Test
    public void testDifferentQuestionsInDifferentCategories(){
        Long category = categoryEJB.createNewCategory("category2");
        Long subCategory = categoryEJB.createNewSubCategory("sub1", category);

        Long subSubCategory1 = categoryEJB.createNewSubSubCategory("subsub1", subCategory);
        Long subSubCategory2 = categoryEJB.createNewSubSubCategory("subsub2", subCategory);
        Long subSubCategory3 = categoryEJB.createNewSubSubCategory("subsub3", subCategory);

        questionEJB.createQuestion(subSubCategory1, "Question", answers, 3);
        questionEJB.createQuestion(subSubCategory1, "Question", answers, 3);
        questionEJB.createQuestion(subSubCategory1, "Question", answers, 3);
        questionEJB.createQuestion(subSubCategory2, "Question", answers, 2);
        questionEJB.createQuestion(subSubCategory2, "Question", answers, 3);
        Long question = questionEJB.createQuestion(subSubCategory3, "Question", answers, 0);

        assertEquals(3, questionEJB.getQuestions(subSubCategory1).size());
        assertEquals(2, questionEJB.getQuestions(subSubCategory2).size());
        assertEquals(1, questionEJB.getQuestions(subSubCategory3).size());
        assertTrue(questionEJB.getQuestions(subSubCategory3).stream().anyMatch(c -> c.getId() == question));
    }

    @Test
    public void testCreateQuestionWithoutValidCategory(){
        assertNull(questionEJB.createQuestion(Long.valueOf(123), "Question", answers, 3));
    }

    @Test
    public void testCreateQuestionWithAnswerOutOfBounds(){
        Long category = categoryEJB.createNewCategory("categoryX");
        Long subCategory = categoryEJB.createNewSubCategory("subX", category);
        Long subSubCategory = categoryEJB.createNewSubSubCategory("subsubX", subCategory);

        Long question = questionEJB.createQuestion(subSubCategory, "Question", answers, 4);
        assertNull(question);
    }
}
