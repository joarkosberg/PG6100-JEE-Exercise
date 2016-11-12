package org.pg6100.restApi.api;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.quiz.ejb.QuestionEJB;
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

    @EJB
    private CategoryEJB categoryEJB;

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
            id = questionEJB.createQuestion(dto.subSubCategory.getId(), dto.question, dto.answers, dto.correctAnswer);
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

        if (dtoID != id)
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        if (!questionEJB.isPresent(id))
            throw new WebApplicationException("Not allowed to create a question with PUT, " +
                    "and cannot find question with id: " + id, 404);

        try {
            questionEJB.updateQuestion(id, dto.subSubCategory.getId(), dto.question, dto.answers, dto.correctAnswer);
        } catch (Exception e) {
            throw wrapException(e);
        }
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

