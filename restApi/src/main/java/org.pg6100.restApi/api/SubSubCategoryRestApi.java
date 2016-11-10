package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/subsubcategories" , description = "Handling of creating and retrieving quiz data")
@Path("/subsubcategories")
@Produces({
        Formats.BASE_JSON
})

public interface SubSubCategoryRestApi {

    @ApiOperation("Retrieve a list of all sub sub categories")
    @GET
    List<SubSubCategoryDto> getAllSubSubCategories();

    @ApiOperation("Create a new sub sub category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Name of the newly create sub sub category")
    Long createSubSubCategory(
            @ApiParam("Name of new sub sub category and parent sub category")
                    SubSubCategoryDto dto);

    @ApiOperation("Get all sub sub categories for a sub category")
    @GET
    @Path("/parent/{id}")
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam("Name of parent sub category")
            @PathParam("id")
                    Long subCategory);


    //GET ID
    //PUT ID
    //PATCH ID
    //DELETE ID


    //GET ID by parent
}
