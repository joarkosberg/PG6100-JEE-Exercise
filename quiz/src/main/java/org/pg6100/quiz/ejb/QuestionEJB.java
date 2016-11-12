package org.pg6100.quiz.ejb;

import org.pg6100.quiz.entity.Question;
import org.pg6100.quiz.entity.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class QuestionEJB {

    @PersistenceContext
    private EntityManager em;

    public Long createQuestion(Long subSubCategoryId, @NotNull String questionText,
                               List<String> answers, int correctAnswer){
        if(answers.size() != 4 || correctAnswer > 3){
            return null;
        }

        SubSubCategory subSubCategory = em.find(SubSubCategory.class, subSubCategoryId);
        if(subSubCategory == null){
            return null;
        }

        Question question = new Question();
        question.setQuestion(questionText);
        question.setAnswers(answers);
        question.setCorrectAnswer(correctAnswer);
        question.setSubSubCategory(subSubCategory);

        em.persist(question);

        return question.getId();
    }

    public boolean updateQuestion(@NotNull Long id, @NotNull Long subSubCategoryId,
                                  @NotNull String questionText, @NotNull List<String> answers, @NotNull int correctAnswer){
        Question q = getQuestion(id);
        if(q == null)
            return false;

        q.setSubSubCategory(em.find(SubSubCategory.class, subSubCategoryId));
        q.setQuestion(questionText);
        q.setAnswers(answers);
        q.setCorrectAnswer(correctAnswer);
        return true;
    }

    public List<Question> getQuestions(Long subSubCategoryId){
        Query query = em.createNamedQuery(Question.GET_QUESTIONS);
        query.setParameter("id", subSubCategoryId);
        return query.getResultList();
    }

    public List<Question> getAllQuestions(){
        Query query = em.createNamedQuery(Question.GET_ALL_QUESTIONS);
        return query.getResultList();
    }

    public boolean isPresent(Long id){
        return em.find(Question.class, id) != null;
    }

    public Question getQuestion(Long id){
        return em.find(Question.class, id);
    }

    public boolean deleteQuestion(@NotNull Long id){
        Question q = em.find(Question.class, id);
        if (q != null) {
            em.remove(q);
            return true;
        }
        return false;
    }
}
