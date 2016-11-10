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

@Api(value = "/categories" , description = "Handling of creating and retrieving quiz data")
@Path("/categories")
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
    @ApiResponse(code = 200, message = "Name of the newly created category")
    String createCategory(
            @ApiParam("Name of category")
                    CategoryDto dto);


    //GET ID
    //PUT ID
    //PATCH ID
    //DELETE ID

    //GET withQuizes

    //GET subsub WithQuizes

    //GET ID subcategories



}
