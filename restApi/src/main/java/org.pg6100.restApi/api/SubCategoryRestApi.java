package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.pg6100.quiz.entity.SubCategory;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

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
    Long createSubCategory(
            @ApiParam("Name of new sub category and parent category")
                    SubCategoryDto dto);

    //GET all subcategories with the given parent specified by id
    @ApiOperation("Get all sub categories for a given category")
    @GET
    @Path("/parent/{id}")
    List<SubCategoryDto> getSubCategories(
            @ApiParam("Name of parent category")
            @PathParam("id")
                    Long category);

    @ApiOperation("Get specified sub category by id")
    @GET
    @Path("/id/{id}")
    SubCategoryDto getSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id);

    @ApiOperation("Update specified sub category by id")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("The sub category that will replace the old one")
                    SubCategoryDto dto);


    //PATCH ID /id/{id}



    @ApiOperation("Delete the sub category with the given id")
    @DELETE
    @Path("/id/{id}")
    void deleteSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id);
    
    //GET all subsubcategories of the subcategory specified by id
    @ApiOperation("Get all sub sub categories for a sub category")
    @GET
    @Path("/id/{id}/subsubcategories")
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam("Name of parent sub category")
            @PathParam("id")
                    Long subCategory);
}
