package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/category" , description = "Handling of creating and retrieving quiz data")
@Path("/category")
@Produces({
        Formats.BASE_JSON
})

public interface CategoryRestApi {


    @ApiOperation("Retrieve a list of all the categories")
    @GET
    List<CategoryDto> getCategories();


    @ApiOperation("Create a new category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly create category")
    String createCategory(
            @ApiParam("Name of category")
                    CategoryDto dto);


    @ApiOperation("Get all sub categories for a category")
    @GET
    @Path("/sub/{category}")
    List<SubCategoryDto> getSubCategories(
            @ApiParam("Name of parent category")
            @PathParam("category")
                    String category);


    @ApiOperation("Create a new sub categories")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly create sub category")
    String createSubCategory(
            @ApiParam("Name of category")
                    CategoryDto dto);


    @ApiOperation("Get all sub sub categories for a category")
    @GET
    @Path("/sub/{category}")
    List<SubCategoryDto> getSubSubCategories(
            @ApiParam("Name of parent category")
            @PathParam("category")
                    String category);


    @ApiOperation("Create a new sub sub category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly create sub sub category")
    String createSubSubCategory(
            @ApiParam("Name of sub sub category")
                    CategoryDto dto);
}
