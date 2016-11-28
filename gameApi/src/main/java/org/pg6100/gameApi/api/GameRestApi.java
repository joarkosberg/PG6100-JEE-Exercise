package org.pg6100.gameApi.api;

import io.swagger.annotations.*;
import org.pg6100.gameApi.dto.GameDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

    @ApiOperation("Retrieve a random question from a category, sub category, sub sub category or random")
    @GET
    List<GameDto> getActiveGames(
            @ApiParam("ID of category, sub category or sub sub category to get games from.")
            @QueryParam("n")
                    Long id);


}
