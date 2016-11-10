package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/subcategories" , description = "Handling of creating and retrieving quiz data")
@Path("/subcategories")
@Produces({
        Formats.BASE_JSON
})

public interface SubCategoryRestApi {

    @ApiOperation("Retrieve a list of all sub categories")
    @GET
    List<SubCategoryDto> getAllSubCategories();

    @ApiOperation("Create a new sub categories")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly created sub category")
    String createSubCategory(
            @ApiParam("Name of new sub category and parent category")
                    SubCategoryDto dto);

    @ApiOperation("Get all sub categories for a given category")
    @GET
    @Path("/parent/{id}")
    List<SubCategoryDto> getSubCategories(
            @ApiParam("Name of parent category")
            @PathParam("id")
                    String category);

    //GET ID
    //PUT ID
    //PATCH ID
    //DELETE ID

    //GET ID subsubcategories
}
