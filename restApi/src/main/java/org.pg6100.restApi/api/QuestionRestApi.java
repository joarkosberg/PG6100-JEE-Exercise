package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
import org.pg6100.restApi.dto.QuestionDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/quizzes" , description = "Handling of creating and retrieving quiz data")
@Path("/quizzes")
@Produces({
        Formats.BASE_JSON
})

public interface QuestionRestApi {

    //GET
    @ApiOperation("Retrieve a list of all the questions")
    @GET
    List<QuestionDto> getAllQuestions();

    //POST
    @ApiOperation("Create a new question")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Id of the newly created question")
    Long createQuestion(
            @ApiParam("Text of question, answers, correctAnswer and which subsubcategory it belongs to")
                    QuestionDto dto);

    @ApiOperation("Get specific question by id")
    @GET
    @Path("/id/{id}")
    QuestionDto getQuestion(
            @ApiParam("Id of the question")
            @PathParam("id")
                    Long id);

    @ApiOperation("Update specified question by id")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateQuestion(
            @ApiParam("Id of question")
            @PathParam("id")
                    Long id,
            @ApiParam("Question to replace the old one")
                    QuestionDto dto);

    //PATCH ID /id/{id}
    @ApiOperation("Modify the text if a Question")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    void patchQuestionText(
            @ApiParam("The unique id of a question")
            @PathParam("id")
                    Long id,
            @ApiParam("New question text")
                    String text);

    @ApiOperation("Delete a question with the given id")
    @DELETE
    @Path("/id/{id}")
    void deleteQuestion(
            @ApiParam("Id of question")
            @PathParam("id")
                    Long id);


        /*
    Deprecated
     */


    /*
• /quizzes/id/{id}
◦ should redirect to /quizzes/{id}
     */
}
