package org.pg6100.restApi.api.implementation;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.pg6100.quiz.ejb.QuestionEJB;
import org.pg6100.quiz.entity.Question;
import org.pg6100.restApi.api.QuestionRestApi;
import org.pg6100.restApi.dto.QuestionDto;
import org.pg6100.restApi.dto.converter.QuestionConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuestionRestImpl implements QuestionRestApi {

    @EJB
    private QuestionEJB questionEJB;

    @Override
    public List<QuestionDto> getAllQuestions() {
        return QuestionConverter.transform(questionEJB.getAllQuestions());
    }

    @Override
    public Long createQuestion(QuestionDto dto) {
        if(!Strings.isNullOrEmpty(dto.id))
            throw new WebApplicationException("Cannot specify id for a newly generated questions", 400);
        if(Strings.isNullOrEmpty(dto.question))
            throw new WebApplicationException("Must specify question", 400);
        if(dto.answers.size() != 4)
            throw new WebApplicationException("Must have 4 possible answers", 400);

        Long id;
        try{
            id = questionEJB.createQuestion(Long.valueOf(dto.subSubCategory.id), dto.question, dto.answers, dto.correctAnswer);
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public QuestionDto getQuestion(Long id) {
        if(!questionEJB.isPresent(id)){
            throw new WebApplicationException("Cannot find question with id: " + id, 404);
        }
        return QuestionConverter.transform(questionEJB.getQuestion(id));
    }

    @Override
    public void updateQuestion(Long id, QuestionDto dto) {
        Long dtoID;
        try {
            dtoID = Long.parseLong(dto.id);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid id: " + dto.id, 400);
        }

        if (!dtoID.equals(id))
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        if (!questionEJB.isPresent(id))
            throw new WebApplicationException("Not allowed to create a question with PUT, " +
                    "and cannot find question with id: " + id, 404);

        try {
            questionEJB.updateQuestion(id, Long.valueOf(dto.subSubCategory.id), dto.question, dto.answers, dto.correctAnswer);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patchQuestionText(Long id, String text) {
        if(!questionEJB.isPresent(id))
            throw new WebApplicationException("Cannot find question with id: " + id, 404);

        if(Strings.isNullOrEmpty(text))
            throw new WebApplicationException("Must specify a new question text!", 400);

        Question question = questionEJB.getQuestion(id);
        questionEJB.updateQuestion(id, question.getSubSubCategory().getId(), text,
                question.getAnswers(), question.getCorrectAnswer());
    }

    @Override
    public void deleteQuestion(Long id) {
        if (!questionEJB.isPresent(id)) {
            throw new WebApplicationException("Cannot find question with id: " + id, 404);
        }
        questionEJB.deleteQuestion(id);
    }

    private WebApplicationException wrapException(Exception e) throws WebApplicationException{
        Throwable cause = Throwables.getRootCause(e);
        if(cause instanceof ConstraintViolationException){
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}

