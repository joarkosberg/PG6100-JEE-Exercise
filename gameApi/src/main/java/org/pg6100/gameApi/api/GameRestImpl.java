package org.pg6100.gameApi.api;

import com.google.common.base.Throwables;
import org.pg6100.gameApi.helper.QuizApiCaller;
import org.pg6100.gameApi.jdbi.GameDAO;
import org.pg6100.gameApi.model.Game;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

public class GameRestImpl implements GameRestApi{

    private static final String QUIZ_PATH = "http://localhost:8080/quiz/api/quizzes/";

    private GameDAO gameDao;

    public GameRestImpl (GameDAO gameDao) {
        this.gameDao = gameDao;
    }

    @Override
    public List<Game> getActiveGames(Integer limit) {
        return gameDao.getAll(limit);
    }

    @Override
    public Response createGame(Integer limit) {
        if(limit < 1)
            throw new WebApplicationException("Cannot set limit lower than 1", 400);

        Long []quizList = QuizApiCaller.getRandomQuizzes(limit);
        if (quizList.length < 1) {
            throw new WebApplicationException("Something went wrong when collecting quizzes for this game", 500);
        }

        Long id;
        try {
            String path = QUIZ_PATH + quizList[0].toString() + "?noAnswer=true";
            id = gameDao.insert(quizList, 0, path);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }

        return Response.status(201)
                .location(URI.create("/game/api/games/" + id))
                .build();
    }

    @Override
    public Game getGame(Long id) {
        Game game = gameDao.findById(id);

        if(game == null)
            throw new WebApplicationException("No game with given id " + id, 404);

        return game;
    }

    @Override
    public Response answerQuiz(Long id, Integer answer) {
        Game game = gameDao.findById(id);

        if(game == null)
            return Response.status(404).build(); // Feil id

        boolean correct = QuizApiCaller.checkAnswer(game.getQuestions()[game.getAnsweredQuestions()], answer);

        if(!correct){
            gameDao.deleteById(id);
            return Response.status(409).build(); // Feil svar
        } else {
            int answeredQuestions = game.getAnsweredQuestions();
            answeredQuestions++;
            if(answeredQuestions >= game.getQuestions().length) {
                gameDao.deleteById(id);
                Response.status(204).build(); //Quiz ferdig
            }

            String path = QUIZ_PATH + game.getQuestions()[answeredQuestions].toString() + "?noAnswer=true";
            gameDao.update(id, answeredQuestions, path);

            return Response.status(200).build(); //Quiz fortsetter
        }
    }

    @Override
    public void deleteGame(Long id) {
        int response = gameDao.deleteById(id);
        if(response == 0)
            throw new WebApplicationException("Id was invalid, nothing deleted", 404);
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
