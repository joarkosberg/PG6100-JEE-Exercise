package org.pg6100.restApi.api;

import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/subsubcategories" , description = "Handling of creating and retrieving quiz data")
@Path("/subsubcategories")
@Produces({
        Formats.BASE_JSON
})

public interface SubSubCategoryRestApi {

    @ApiOperation("Retrieve a list of all sub sub categories")
    @GET
    List<SubSubCategoryDto> getAllSubSubCategories(
            @QueryParam("withQuizzes")
                    Boolean withQuizzes
    );



    @ApiOperation("Create a new sub sub category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Id of the newly create sub sub category")
    Long createSubSubCategory(
            @ApiParam("Name of new sub sub category and parent sub category")
                    SubSubCategoryDto dto);



    @ApiOperation("Get specified sub sub category by id")
    @GET
    @Path("/{id}")
    SubSubCategoryDto getSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id);



    @ApiOperation("Update specified sub sub category by id")
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("The sub sub category that will replace the old one")
                    SubSubCategoryDto dto);



    @ApiOperation("Modify parent sub category of sub sub category")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN) // could have had a custom type here, but then would need unmarshaller for it
    void patchSubSubCategoryParent(
            @ApiParam("The unique id of a sub sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("New parent sub category id")
                    Long parentId);



    @ApiOperation("Delete the sub sub category with the given id")
    @DELETE
    @Path("/{id}")
    void deleteSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id);



        /*
    Deprecated
     */
    @ApiOperation("Get specified sub sub category by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id);



    @ApiOperation("Update specified sub sub category by id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Deprecated
    Response deprecatedUpdateSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("The sub sub category that will replace the old one")
                    SubSubCategoryDto dto);



    @ApiOperation("Modify parent sub category of sub sub category")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PATCH
    @Path("/id/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Deprecated
    Response deprecatedPatchSubSubCategoryParent(
            @ApiParam("The unique id of a sub sub category")
            @PathParam("id")
                    Long id,
            @ApiParam("New parent sub category id")
                    Long parentId);



    @ApiOperation("Delete the sub sub category with the given id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedDeleteSubSubCategory(
            @ApiParam("Id of sub sub category")
            @PathParam("id")
                    Long id);



    //GET all subsubcategories with the given subcategory parent specified by id
    @ApiOperation("Get all sub sub categories for a sub category")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response deprecatedGetSubSubCategories(
            @ApiParam("Name of parent sub category")
            @PathParam("id")
                    Long subCategory);
}
