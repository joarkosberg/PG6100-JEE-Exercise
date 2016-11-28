package org.pg6100.gameApi.api;

import io.swagger.annotations.*;
import org.pg6100.gameApi.model.Game;

import javax.ws.rs.*;
import java.util.List;

@Api(value = "/games" , description = "Handling game data")
@Path("/games")
@Produces({
        Formats.V2_JSON,
        Formats.BASE_JSON
})

public interface GameRestApi {
    String ID_PARAM ="The numeric id of the news";

    @ApiOperation("Retrieve a random question from a category, sub category, sub sub category or random")
    @GET
    List<Game> getActiveGames(
            @ApiParam("Limit lines of output")
            @DefaultValue("5")
            @QueryParam("n")
                    Integer limit);
}
