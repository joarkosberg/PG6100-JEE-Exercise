package org.pg6100.restApi.api;

import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.quiz.ejb.QuestionEJB;
import org.pg6100.restApi.dto.QuestionDto;
import org.pg6100.restApi.dto.converter.QuestionConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuestionRestImpl implements QuestionRestApi {

    @EJB
    private QuestionEJB questionEJB;

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<QuestionDto> getQuestions() {
        return QuestionConverter.transform(questionEJB.getAllQuestions());
    }
}

