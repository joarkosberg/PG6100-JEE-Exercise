package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.QuestionDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    List<QuestionDto> getQuestions();

    //POST



    //GET ID
    //PUT ID
    //PATCH ID
    //DELETE ID

}
