package org.pg6100.quiz.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class QuestionEJBTest extends EJBTestBase{

    @Test
    public void testCreateQuestion(){
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

    @Test
    public void testUpdateQuestion(){
        Long category = categoryEJB.createNewCategory("category");
        Long subCategory = categoryEJB.createNewSubCategory("sub", category);
        Long subSubCategory = categoryEJB.createNewSubSubCategory("subsub", subCategory);

        String questionText = "Hei hei";
        Long question = questionEJB.createQuestion(subSubCategory, questionText, answers, 3);
        assertEquals(questionText, questionEJB.getQuestion(question).getQuestion());

        String newQuestionText = "Shit is new";
        assertTrue(questionEJB.updateQuestion(question, subSubCategory, newQuestionText, answers, 3));
        assertNotEquals(questionText, questionEJB.getQuestion(question).getQuestion());
        assertEquals(newQuestionText, questionEJB.getQuestion(question).getQuestion());
    }
}
