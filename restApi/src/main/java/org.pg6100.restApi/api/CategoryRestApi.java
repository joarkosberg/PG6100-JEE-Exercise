package org.pg6100.restApi.api;

import io.swagger.annotations.*;
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
    List<CategoryDto> getCategories(
            @ApiParam("True if only want categories with quizzes")
            @QueryParam("withQuizzes")
                    Boolean withQuizzes
    );



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
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id,
            @ApiParam("The category that will replace the old one")
                    CategoryDto dto);



    @ApiOperation("Modify the name of a category")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    void patchCategoryName(
                    @ApiParam("The unique id of a category")
                    @PathParam("id")
                            Long id,
                    @ApiParam("New category name")
                            String text);



    @ApiOperation("Delete a category with the given id")
    @DELETE
    @Path("/{id}")
    void deleteCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);



    //GET all subcategories of the category specified by id
    @ApiOperation("Get all sub categories of a specified category id")
    @GET
    @Path("/{id}/subcategories")
    List<SubCategoryDto> getSubCategories(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);



    /*
    Deprecated
     */
    @ApiOperation("Get specified category by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);



    @ApiOperation("Update specified category by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Deprecated
    Response deprecatedUpdateCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id,
            @ApiParam("The category that will replace the old one")
                    CategoryDto dto);



    @ApiOperation("Modify the name of a category")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PATCH
    @Path("/id/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Deprecated
    Response deprecatedPatchCategoryName(
            @ApiParam("The unique id of a category")
            @PathParam("id")
                    Long id,
            @ApiParam("New category name")
                    String text);



    @ApiOperation("Delete a category with the given id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedDeleteCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);



    @ApiOperation("Get all categories with at least one sub category with at least one sub sub category with at least one question")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/withquizzes")
    @Deprecated
    Response deprecatedGetCategoriesWithQuizzes();



    @ApiOperation("Get all sub categories of a specified category id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/id/{id}/subcategories")
    @Deprecated
    Response deprecatedGetSubCategories(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);



    //GET all subsubcategories with at least one quiz
    @ApiOperation("Get all sub sub categories with at least one question")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/withquizzes/subsubcategories")
    @Deprecated
    Response deprecatedGetSubSubCategoriesWithQuizzes();
}
