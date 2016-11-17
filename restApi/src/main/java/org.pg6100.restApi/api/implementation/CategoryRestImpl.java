package org.pg6100.restApi.api.implementation;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import io.swagger.annotations.ApiParam;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.restApi.api.CategoryRestApi;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CategoryRestImpl implements CategoryRestApi {

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<CategoryDto> getCategories(Boolean withQuizzes) {
        if(withQuizzes != null)
            if (withQuizzes)
                return CategoryConverter.transform(categoryEJB.getCategoriesWithQuestions());

        return CategoryConverter.transform(categoryEJB.getAllCategories());
    }

    @Override
    public Long createCategory(CategoryDto dto) {
        if(!Strings.isNullOrEmpty(dto.id))
            throw new WebApplicationException("Cannot specify id for a newly generated categories", 400);
        if(Strings.isNullOrEmpty(dto.name))
            throw new WebApplicationException("Must specify name for category", 400);

        Long name;
        try{
            name = categoryEJB.createNewCategory(dto.name);
        }catch (Exception e){
            throw wrapException(e);
        }

        return name;
    }

    @Override
    public CategoryDto getCategory(Long id) {
        if (!categoryEJB.isCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find category with id: " + id, 404);
        }
        return CategoryConverter.transform(categoryEJB.getCategory(id));
    }

    @Override
    public void updateCategory(Long id, CategoryDto dto) {
        Long dtoID;
        try {
            dtoID = Long.parseLong(dto.id);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid id: " + dto.id, 400);
        }

        if (!dtoID.equals(id))
            throw new WebApplicationException("Not allowed to change the id of the resource1", 409);
        if (!categoryEJB.isCategoryPresent(id))
            throw new WebApplicationException("Not allowed to create a news with PUT, and cannot find category with id: " + dtoID, 404);

        try {
            categoryEJB.updateCategory(id, dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patchCategoryName(Long id, String name) {
        if(!categoryEJB.isCategoryPresent(id))
            throw new WebApplicationException("Cannot find category with id: " + id, 404);

        if(Strings.isNullOrEmpty(name))
            throw new WebApplicationException("Must specify a new category name!", 400);

        categoryEJB.updateCategory(id, name);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryEJB.isCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find category with id: " + id, 404);
        }
        categoryEJB.delete(id);
    }

    @Override
    public List<SubCategoryDto> getSubCategories(Long id) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategories(id));
    }

    /*
    Deprecated
     */
    @Override
    public Response deprecatedGetCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedUpdateCategory(Long id, CategoryDto dto) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedPatchCategoryName(Long id, String text) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedDeleteCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedGetCategoriesWithQuizzes() {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories")
                        .queryParam("withQuizzes", true).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubCategories(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + id + "/subcategories").build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubCategoriesWithQuizzes() {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories")
                        .queryParam("withQuizzes", true).build())
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
