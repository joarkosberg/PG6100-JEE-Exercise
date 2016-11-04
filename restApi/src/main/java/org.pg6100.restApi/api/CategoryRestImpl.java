package org.pg6100.restApi.api;

import com.google.common.base.Throwables;
import io.swagger.annotations.ApiParam;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;
import org.pg6100.restApi.dto.converter.CategoryConverter;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.converter.SubCategoryConverter;
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
public class CategoryRestImpl implements CategoryRestApi {

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<CategoryDto> getCategories() {
        return CategoryConverter.transform(categoryEJB.getAllCategories());
    }

    @Override
    public String createCategory(CategoryDto dto) {
        if(dto.name == null && dto.name.trim().isEmpty()){
            throw new WebApplicationException("Must specify name!", 400);
        }

        String name;
        try{
            name = categoryEJB.createNewCategory(dto.name);
        }catch (Exception e){
            throw wrapException(e);
        }

        return name;
    }

    @Override
    public List<SubCategoryDto> getSubCategories(String category) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategories(category));
    }

    @Override
    public String createSubCategory(SubCategoryDto dto) {
        if(dto.name == null && dto.name.trim().isEmpty()){
            throw new WebApplicationException("Must specify name!", 400);
        }

        String name;
        try{
            name = categoryEJB.createNewSubCategory(dto.name, dto.category.getName());
        }catch (Exception e){
            throw wrapException(e);
        }

        return name;
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategories(String subCategory) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategories(subCategory));
    }

    @Override
    public String createSubSubCategory(SubSubCategoryDto dto) {
        if(dto.name == null && dto.name.trim().isEmpty()){
            throw new WebApplicationException("Must specify name!", 400);
        }

        String name;
        try{
            name = categoryEJB.createNewSubSubCategory(dto.name, dto.subCategory.getName());
        }catch (Exception e){
            throw wrapException(e);
        }

        return name;
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