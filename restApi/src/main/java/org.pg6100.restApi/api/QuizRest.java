package org.pg6100.restApi.api;

import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.restApi.dto.CategoryConverter;
import org.pg6100.restApi.dto.CategoryDto;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuizRest implements QuizRestApi{

    @EJB
    private CategoryEJB ejb;

    @Override
    public List<CategoryDto> getCategories() {
        return CategoryConverter.transform(ejb.getAllCategories());
    }
}