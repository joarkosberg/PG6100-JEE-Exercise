package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
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
    @ApiResponse(code = 200, message = "Id of the newly create sub sub category")
    Long createSubSubCategory(
            @ApiParam("Name of new sub sub category and parent sub category")
                    SubSubCategoryDto dto);

    @ApiOperation("Get specified sub sub category by id")
    @GET
    @Path("/id/{id}")
    SubSubCategoryDto getSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id);

    @ApiOperation("Update specified sub sub category by id")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("The sub sub category that will replace the old one")
                    SubSubCategoryDto dto);

    //PATCH ID /id/{id}
    @ApiOperation("Modify parent sub category of sub sub category")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN) // could have had a custom type here, but then would need unmarshaller for it
    void patchSubSubCategoryParent(
            @ApiParam("The unique id of a sub sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("New parent sub category id")
                    Long parentId);

    @ApiOperation("Delete the sub sub category with the given id")
    @DELETE
    @Path("/id/{id}")
    void deleteSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id);

    //GET all subsubcategories with the given subcategory parent specified by id
    @ApiOperation("Get all sub sub categories for a sub category")
    @GET
    @Path("/parent/{id}")
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam("Name of parent sub category")
            @PathParam("id")
                    Long subCategory);
}
