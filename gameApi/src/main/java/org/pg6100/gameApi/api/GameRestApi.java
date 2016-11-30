package org.pg6100.gameApi.api;

import io.swagger.annotations.*;
import org.pg6100.gameApi.model.Game;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/games" , description = "Handling game data")
@Path("/games")
@Produces({
        Formats.V2_JSON,
        Formats.BASE_JSON
})

public interface GameRestApi {



    @ApiOperation("Retrieve a set of active games")
    @GET
    List<Game> getActiveGames(
            @ApiParam("Limit games to get(default 5)")
            @DefaultValue("5")
            @QueryParam("n")
                    Integer limit);



    @ApiOperation("Create a new active game")
    @POST
    Response createGame(
            @ApiParam("Number of questions wanted (default 5)")
            @DefaultValue("5")
            @QueryParam("n")
                    Integer limit);



    @ApiOperation("Retrieve game by id")
    @GET
    @Path("/{id}")
    Game getGame(
            @ApiParam("Id of wanted game")
            @PathParam("id")
                    Long id);



    @ApiOperation("Answer a question")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Question correct, next question!"),
            @ApiResponse(code = 204, message = "All questions answered correct! Game is finished."),
            @ApiResponse(code = 404, message = "No game with that id"),
            @ApiResponse(code = 409, message = "Wrong answer, game over!")
    })
    @POST
    @Path("/{id}")
    Response answerQuiz(
            @ApiParam("Id of game")
            @PathParam("id")
                    Long id,
            @ApiParam("Your answer from 0 - 3")
            @QueryParam("answer")
            @Min(0)
            @Max(3)
                    Integer answer);



    @ApiOperation("Retrieve a set of active games")
    @DELETE
    @Path("/{id}")
    void deleteGame(
            @ApiParam("Limit games to get(default 5)")
            @PathParam("id")
                    Long id);
}
