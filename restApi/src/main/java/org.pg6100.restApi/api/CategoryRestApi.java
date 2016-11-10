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
    Long createCategory(
            @ApiParam("Name of category")
                    CategoryDto dto);

    //GET ID
    @ApiOperation("Get specified category by id")
    @GET
    @Path("/id/{id}")
    CategoryDto getCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id);

    //PUT ID
    @ApiOperation("Get specified category by id")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long id,
            @ApiParam("The category that will replace the old one")
                    CategoryDto dto);




    //PATCH ID


    //DELETE ID
    @ApiOperation("Delete a category with the given id")
    @DELETE
    @Path("/id/{id}")
    void deleteCategory(
            @ApiParam("Id of category")
            @PathParam("id")
                    Long name);


    //GET withQuizes

    //GET subsub WithQuizes

    //GET ID subcategories



}
