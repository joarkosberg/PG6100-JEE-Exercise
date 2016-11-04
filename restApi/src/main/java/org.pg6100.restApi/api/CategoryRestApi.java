package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/category" , description = "Handling of creating and retrieving quiz data")
@Path("/category")
@Produces({
        Formats.BASE_JSON
})

public interface CategoryRestApi {

    /*
    Category
     */
    @ApiOperation("Retrieve a list of all the categories")
    @GET
    List<CategoryDto> getCategories();

    @ApiOperation("Create a new category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly created category")
    String createCategory(
            @ApiParam("Name of category")
                    CategoryDto dto);



    /*
    Sub Category
     */
    @ApiOperation("Get all sub categories for a category")
    @GET
    @Path("/sub/{category}")
    List<SubCategoryDto> getSubCategories(
            @ApiParam("Name of parent category")
            @PathParam("category")
                    String category);

    @ApiOperation("Create a new sub categories")
    @POST
    @Path("/sub")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly created sub category")
    String createSubCategory(
            @ApiParam("Name of new sub category and parent category")
                    SubCategoryDto dto);



    /*
    Sub Category
     */
    @ApiOperation("Get all sub sub categories for a sub category")
    @GET
    @Path("/subsub/{subCategory}")
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam("Name of parent sub category")
            @PathParam("subCategory")
                    String subCategory);

    @ApiOperation("Create a new sub sub category")
    @POST
    @Path("/subsub")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly create sub sub category")
    String createSubSubCategory(
            @ApiParam("Name of new sub sub category and parent sub category")
                    SubSubCategoryDto dto);
}
