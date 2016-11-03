package org.pg6100.quiz.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

    @Test
    public void testCreateQuestions(){
        String category = categoryEJB.createNewCategory("category1");
        String subCategory = categoryEJB.createNewSubCategory("sub", category);
        String subSubCategory = categoryEJB.createNewSubSubCategory("subsub", subCategory);

        Long question = questionEJB.createQuestion(subSubCategory, "Question", answers, 3);
        assertNotNull(question);
        assertEquals(1, questionEJB.getQuestions(subSubCategory).size());
    }

    @Test
    public void testDifferentQuestionsInDifferentCategories(){
        String category = categoryEJB.createNewCategory("category2");
        String subCategory = categoryEJB.createNewSubCategory("sub1", category);

        String subSubCategory1 = categoryEJB.createNewSubSubCategory("subsub1", subCategory);
        String subSubCategory2 = categoryEJB.createNewSubSubCategory("subsub2", subCategory);
        String subSubCategory3 = categoryEJB.createNewSubSubCategory("subsub3", subCategory);

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
        assertNull(questionEJB.createQuestion("subety", "Question", answers, 3));
    }

    @Test
    public void testCreateQuestionWithAnswerOutOfBounds(){
        String category = categoryEJB.createNewCategory("categoryX");
        String subCategory = categoryEJB.createNewSubCategory("subX", category);
        String subSubCategory = categoryEJB.createNewSubSubCategory("subsubX", subCategory);

        Long question = questionEJB.createQuestion(subSubCategory, "Question", answers, 4);
        assertNull(question);
    }
}
