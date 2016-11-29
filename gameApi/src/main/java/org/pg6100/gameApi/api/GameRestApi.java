package org.pg6100.gameApi.api;

import io.swagger.annotations.*;
import org.pg6100.gameApi.model.Game;

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
    String ID_PARAM ="The numeric id of the news";

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
}
