package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/categories" , description = "Handling of creating and retrieving category data")
@Path("/categories")
@Produces({
        Formats.BASE_JSON
})

public interface CategoryRestApi {

    String ID_PARAM = "Id of category";

    //GET
    @ApiOperation("Retrieve a list of all the categories")
    @GET
    List<CategoryDto> getCategories();

    //POST
    @ApiOperation("Create a new category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly created category")
    Long createCategory(
            @ApiParam("Name of category")
                    CategoryDto dto);

    @ApiOperation("Get specified category by id")
    @GET
    @Path("/{id}")
    CategoryDto getCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("Update specified category by id")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id,
            @ApiParam("The category that will replace the old one")
                    CategoryDto dto);

    //PATCH ID /id/{id}
    @ApiOperation("Modify the name of a category")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN) // could have had a custom type here, but then would need unmarshaller for it
    void patchCategoryName(
                    @ApiParam("The unique id of a category")
                    @PathParam("id")
                            Long id,
                    @ApiParam("New category name")
                            String text);

    @ApiOperation("Delete a category with the given id")
    @DELETE
    @Path("/id/{id}")
    void deleteCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);

    //GET all categories that have at least one subcategory with at least one subsubcategory with at least one quiz.
    @ApiOperation("Get all categories with at least one sub category with at least one sub sub category with at least one question")
    @GET
    @Path("?withquizzes")
    List<CategoryDto> getCategoriesWithQuizzes();

    //GET all subsubcategories with at least one quiz
    @ApiOperation("Get all sub sub categories with at least one question")
    @GET
    @Path("/withquizzes/subsubcategories")
    List<SubSubCategoryDto> getSubSubCategoriesWithQuizzes();

    //GET all subcategories of the category specified by id
    @ApiOperation("Get all sub categories of a specified category id")
    @GET
    @Path("/id/{id}/subcategories")
    List<SubCategoryDto> getSubCategories(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);


    /*
    Deprecated
     */
    @ApiOperation("Get specified category by id")
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);

    @ApiOperation("Get all categories with at least one sub category with at least one sub sub category with at least one question")
    @GET
    @Path("/withquizzes")
    Response deprecatedGetCategoriesWithQuizzes();

    /*
• /categories/withQuizzes/subsubcategories
should redirect to /subsubcategories?withQuizzes
• /categories/id/{id}/subcategories
should redirect to /categories/{id}/subcategories
     */
}
