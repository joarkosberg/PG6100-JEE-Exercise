package org.pg6100.quizApi.api.implementation;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.quizApi.api.SubCategoryRestApi;
import org.pg6100.quizApi.dto.SubCategoryDto;
import org.pg6100.quizApi.dto.SubSubCategoryDto;
import org.pg6100.quizApi.dto.converter.SubCategoryConverter;
import org.pg6100.quizApi.dto.converter.SubSubCategoryConverter;
import org.pg6100.quizApi.api.SubCategoryRestApi;
import org.pg6100.quizApi.dto.SubCategoryDto;
import org.pg6100.quizApi.dto.SubSubCategoryDto;
import org.pg6100.quizApi.dto.converter.SubCategoryConverter;
import org.pg6100.quizApi.dto.converter.SubSubCategoryConverter;

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
public class SubCategoryRestImpl implements SubCategoryRestApi {

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<SubCategoryDto> getAllSubCategories() {
        return SubCategoryConverter.transform(categoryEJB.getAllSubCategories());
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
        if (!categoryEJB.isSubCategoryPresent(id)) //Will probably never get hit.
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

    /*
    Deprecated
     */
    @Override
    public Response deprecatedGetSubCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedUpdateSubCategory(Long id, SubCategoryDto dto) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedPatchSubCategoryName(Long id, String name) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedDeleteSubCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubCategories(Long subCategory) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories/" + subCategory + "/subsubcategories").build())
                .build();
    }

    @Override
    public Response deprecatedGetSubCategories(Long category) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + category + "/subcategories").build())
                .build();
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
