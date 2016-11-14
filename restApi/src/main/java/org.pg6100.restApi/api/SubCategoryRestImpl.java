package org.pg6100.restApi.api;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.restApi.dto.SubCategoryDto;
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
public class SubCategoryRestImpl implements SubCategoryRestApi{

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<SubCategoryDto> getAllSubCategories() {
        return SubCategoryConverter.transform(categoryEJB.getAllSubCategories());
    }

    @Override
    public List<SubCategoryDto> getSubCategories(Long category) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategories(category));
    }

    @Override
    public SubCategoryDto getSubCategory(Long id) {
        if(!categoryEJB.isSubCategoryPresent(id)){
            throw new WebApplicationException("Cannot find sub category with id: " + id, 404);
        }
        return SubCategoryConverter.transform(categoryEJB.getSubCategory(id));
    }

    @Override
    public void updateSubCategory(Long id, SubCategoryDto dto) {
        Long dtoID;
        try {
            dtoID = Long.parseLong(dto.id);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid id: " + dto.id, 400);
        }

        if (!dtoID.equals(id))
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        if (!categoryEJB.isSubCategoryPresent(id))
            throw new WebApplicationException("Not allowed to create a sub category with PUT, " +
                    "and cannot find sub category with id: " + dtoID, 404);

        try {
            categoryEJB.updateSubCategory(id, dto.name, Long.valueOf(dto.category.id));
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patchSubCategoryName(Long id, String name) {
        if(!categoryEJB.isSubCategoryPresent(id))
            throw new WebApplicationException("Cannot find sub category with id: " + id, 404);

        if(Strings.isNullOrEmpty(name))
            throw new WebApplicationException("Must specify a new sub category name!", 400);

        categoryEJB.updateSubCategoryName(id, name);
    }

    @Override
    public void deleteSubCategory(Long id) {
        if (!categoryEJB.isSubCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find sub category with id: " + id, 404);
        }
        categoryEJB.delete(id);
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategories(Long subCategory) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategories(subCategory));
    }

    @Override
    public Long createSubCategory(SubCategoryDto dto) {
        if(!Strings.isNullOrEmpty(dto.id))
            throw new WebApplicationException("Cannot specify id for a newly generated sub categories", 400);
        if(Strings.isNullOrEmpty(dto.name)){
            throw new WebApplicationException("Must specify name of sub category", 400);
        }

        Long id;
        try{
            System.out.println(Long.valueOf(dto.category.id) + " ID");
            System.out.println(dto.name + " NAME");
            id = categoryEJB.createNewSubCategory(dto.name, Long.valueOf(dto.category.id));
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
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
