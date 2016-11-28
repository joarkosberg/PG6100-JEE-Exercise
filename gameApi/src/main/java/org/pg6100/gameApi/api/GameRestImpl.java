package org.pg6100.gameApi.api;

import com.google.common.base.Throwables;
import io.swagger.annotations.ApiParam;
import org.pg6100.gameApi.jdbi.GameDAO;
import org.pg6100.gameApi.model.Game;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class GameRestImpl implements GameRestApi{

    private GameDAO gameDao;

    public GameRestImpl (GameDAO gameDao) {
        this.gameDao = gameDao;
    }

    @Override
    public List<Game> getActiveGames(Integer limit) {
        return gameDao.getAll(limit);
    }

    /*

    String code = "302f56c7ad8e8c82";
        String country = "Norway";
        String city = "Oslo";

        URI uri = UriBuilder
                .fromUri("http://api.wunderground.com/api/"
                        + code +"/geolookup/conditions/forecast/q/"+country+"/"+city+".json")
                .port(80)
                .build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request("application/json").get();

        String result = response.readEntity(String.class);
        System.out.println("Result as string : " + result);

    //just extract one element of interest
    JsonParser parser = new JsonParser();
    JsonObject json =(JsonObject) parser.parse(result);
    String temperature = json.get("current_observation").getAsJsonObject().get("temp_c").getAsString();

        System.out.println("Temperature in Oslo: "+temperature+" C'");
     */
    private WebApplicationException wrapException(Exception e) throws WebApplicationException {
        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
