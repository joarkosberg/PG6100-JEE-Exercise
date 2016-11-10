package org.pg6100.restApi.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.restApi.dto.SubSubCategoryDto;
import org.pg6100.restApi.dto.converter.SubSubCategoryConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubSubCategoryRestImpl implements SubSubCategoryRestApi{

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<SubSubCategoryDto> getAllSubSubCategories() {
        return SubSubCategoryConverter.transform(categoryEJB.getAllSubSubCategories());
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategories(Long subCategory) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategories(subCategory));
    }

    @Override
    public Long createSubSubCategory(SubSubCategoryDto dto) {
        if(dto.name == null && dto.name.trim().isEmpty()){
            throw new WebApplicationException("Must specify name!", 400);
        }

        Long id;
        try{
            id = categoryEJB.createNewSubSubCategory(dto.name, dto.subCategory.getId());
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
    }

    private WebApplicationException wrapException(Exception e) throws WebApplicationException{
        Throwable cause = Throwables.getRootCause(e);
        if(cause instanceof ConstraintViolationException){
            return new WebApplicationException("Invalid constraints on input: "+cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
