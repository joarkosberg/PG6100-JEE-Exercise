package org.pg6100.restApi.api.implementation;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import io.swagger.annotations.ApiParam;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.restApi.api.SubSubCategoryRestApi;
import org.pg6100.restApi.dto.SubSubCategoryDto;
import org.pg6100.restApi.dto.converter.SubSubCategoryConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubSubCategoryRestImpl implements SubSubCategoryRestApi {

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<SubSubCategoryDto> getAllSubSubCategories(Boolean withQuizzes) {
        if(withQuizzes != null)
            if(withQuizzes)
                return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoriesWithQuestions());
        return SubSubCategoryConverter.transform(categoryEJB.getAllSubSubCategories());
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
            id = categoryEJB.createNewSubSubCategory(dto.name, Long.valueOf(dto.subCategory.id));
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
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

        if (!dtoID.equals(id))
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        if (!categoryEJB.isSubSubCategoryPresent(id))
            throw new WebApplicationException("Not allowed to create a sub sub category with PUT, " +
                    "and cannot find sub sub category with id: " + dtoID, 404);

        try {
            categoryEJB.updateSubSubCategory(id, dto.name, Long.valueOf(dto.subCategory.id));
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

        categoryEJB.updateSubSubCategory(id, categoryEJB.getSubSubCategory(id).getName(), parentId);
    }

    @Override
    public void deleteSubSubCategory(Long id) {
        if (!categoryEJB.isSubSubCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find sub sub category with id: " + id, 404);
        }
        categoryEJB.delete(id);
    }

    /*
    Deprecated
     */
    @Override
    public Response deprecatedGetSubSubCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedUpdateSubSubCategory(Long id, SubSubCategoryDto dto) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedPatchSubSubCategoryParent(Long id, Long parentId) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedDeleteSubSubCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubCategories(Long subCategory) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories/" + subCategory + "/subsubcategories").build())
                .build();
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
