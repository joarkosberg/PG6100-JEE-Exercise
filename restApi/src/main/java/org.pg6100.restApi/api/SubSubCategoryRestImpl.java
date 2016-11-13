package org.pg6100.restApi.api;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import io.swagger.annotations.ApiParam;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.restApi.dto.SubSubCategoryDto;
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
    public SubSubCategoryDto getSubSubCategory(Long id) {
        if(!categoryEJB.isSubSubCategoryPresent(id)){
            throw new WebApplicationException("Cannot find sub sub category with id: " + id, 404);
        }
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategory(id));
    }

    @Override
    public void updateSubSubCategory(Long id, SubSubCategoryDto dto) {
        Long dtoID;
        try {
            dtoID = Long.parseLong(dto.id);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid id: " + dto.id, 400);
        }

        if (dtoID != id)
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        if (!categoryEJB.isSubSubCategoryPresent(id))
            throw new WebApplicationException("Not allowed to create a sub sub category with PUT, " +
                    "and cannot find sub sub category with id: " + dtoID, 404);

        try {
            categoryEJB.updateSubSubCategory(id, dto.name, dto.subCategory);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patchSubSubCategoryParent(Long id, Long parentId) {
        if(!categoryEJB.isSubSubCategoryPresent(id))
            throw new WebApplicationException("Cannot find sub sub category with id: " + id, 404);

        if(!categoryEJB.isSubCategoryPresent(parentId))
            throw new WebApplicationException("Cannot find parent sub category with id: " + id, 404);

        categoryEJB.updateSubSubCategory(id, categoryEJB.getSubSubCategory(id).getName(),
                categoryEJB.getSubCategory(parentId));
    }

    @Override
    public void deleteSubSubCategory(Long id) {
        if (!categoryEJB.isSubSubCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find sub sub category with id: " + id, 404);
        }
        categoryEJB.delete(id);
    }

    @Override
    public Long createSubSubCategory(SubSubCategoryDto dto) {
        if(!Strings.isNullOrEmpty(dto.id))
            throw new WebApplicationException("Cannot specify id for a newly generated sub sub categories", 400);
        if(Strings.isNullOrEmpty(dto.name)){
            throw new WebApplicationException("Must specify name of sub sub category", 400);
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
