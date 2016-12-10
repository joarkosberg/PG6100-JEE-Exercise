package org.pg6100.quizApi.api;

import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import org.pg6100.quizApi.dto.SubCategoryDto;
import org.pg6100.quizApi.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/subcategories" , description = "Handling of creating and retrieving quiz data")
@Path("/subcategories")
@Produces({
        Formats.V1_JSON,
        Formats.BASE_JSON
})

public interface SubCategoryRestApi {



    @ApiOperation("Retrieve a list of all sub categories")
    @GET
    List<SubCategoryDto> getAllSubCategories();



    @ApiOperation("Create a new sub categories")
    @POST
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 200, message = "Id of the newly created sub category")
    Long createSubCategory(
            @ApiParam("Name of new sub category and parent category")
                    SubCategoryDto dto);



    @ApiOperation("Get specified sub category by id")
    @GET
    @Path("/{id}")
    SubCategoryDto getSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id);



    @ApiOperation("Update specified sub category by id")
    @PUT
    @Path("/{id}")
    @Consumes(Formats.BASE_JSON)
    void updateSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("The sub category that will replace the old one")
                    SubCategoryDto dto);



    @ApiOperation("Modify the name of a sub category")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    void patchSubCategoryName(
            @ApiParam("The unique id of a sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("New sub category name")
                    String name);



    @ApiOperation("Delete the sub category with the given id")
    @DELETE
    @Path("/{id}")
    void deleteSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id);



    //GET all subsubcategories of the subcategory specified by id
    @ApiOperation("Get all sub sub categories for a sub category")
    @GET
    @Path("/{id}/subsubcategories")
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam("Name of parent sub category")
            @PathParam("id")
                    Long subCategory);



    /*
    Deprecated
     */
    @ApiOperation("Get specified sub category by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id);



    @ApiOperation("Update specified sub category by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Deprecated
    Response deprecatedUpdateSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("The sub category that will replace the old one")
                    SubCategoryDto dto);



    @ApiOperation("Modify the name of a sub category")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PATCH
    @Path("/id/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Deprecated
    Response deprecatedPatchSubCategoryName(
            @ApiParam("The unique id of a sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("New sub category name")
                    String name);



    @ApiOperation("Delete the sub category with the given id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedDeleteSubCategory(
            @ApiParam("Id of sub category")
            @PathParam("id")
                    Long id);



    @ApiOperation("Get all sub sub categories for a sub category")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/id/{id}/subsubcategories")
    @Deprecated
    Response deprecatedGetSubSubCategories(
            @ApiParam("Name of parent sub category")
            @PathParam("id")
                    Long subCategory);



    //GET all subcategories with the given parent specified by id
    @ApiOperation("Get all sub categories for a given category")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response deprecatedGetSubCategories(
            @ApiParam("Name of parent category")
            @PathParam("id")
                    Long category);
}
