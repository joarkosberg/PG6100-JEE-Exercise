package org.pg6100.quiz.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class EJBTestBase {
    protected List<String> answers = Arrays.asList("a1", "a2", "a3", "a4");

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.pg6100.quiz")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    protected CategoryEJB categoryEJB;

    @EJB
    protected QuestionEJB questionEJB;

    @Before
    @After
    public void cleanDatabase() {
        questionEJB.getAllQuestions().stream().forEach(n -> questionEJB.deleteQuestion(n.getId()));
        assertEquals(0, questionEJB.getAllQuestions().size());
        categoryEJB.getAllSubSubCategories().stream().forEach(n -> categoryEJB.deleteSubSubCategory(n.getId()));
        assertEquals(0, categoryEJB.getAllSubSubCategories().size());
        categoryEJB.getAllSubCategories().stream().forEach(n -> categoryEJB.deleteSubCategory(n.getId()));
        assertEquals(0, categoryEJB.getAllSubCategories().size());
        categoryEJB.getAllCategories().stream().forEach(n -> categoryEJB.deleteCategory(n.getId()));
        assertEquals(0, categoryEJB.getAllCategories().size());
    }
}
