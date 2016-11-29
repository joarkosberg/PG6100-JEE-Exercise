package org.pg6100.gameApi.helper;

import com.google.gson.Gson;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuizApiCaller {
    private static final String BASE_PATH = "localhost:8080/quiz/api/";
    private static final String RANDOMQUIZZES_PATH = "/randomQuizzes";
    private static final String SUBSUBCATEGORIES_PATH = "/subsubcategories";

    public static Long []getRandomQuizzes(Integer n) {
        List<Long> subSubCategories = getRandomSubSubcategories(n);

        Response response;
        if(subSubCategories.size() < 1)
            throw new WebApplicationException("Could not find any subsubcategories with " + n + " quizzes", 404);
        else if(subSubCategories.size() == 1)
            response = getQuizzes(n, subSubCategories.get(0));
        else{
            Random random = new Random();
            response = getQuizzes(n, subSubCategories.get(random.nextInt(subSubCategories.size())));
        }

        Long []quizList = response.readEntity(Long[].class);
        return quizList;
    }

    private static Response getQuizzes(Integer limit, Long category) {
        URI uri = UriBuilder.fromUri(BASE_PATH + RANDOMQUIZZES_PATH)
                .queryParam("n", limit)
                .queryParam("filter", category)
                .build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).post(null);
        checkIfError(response.getStatusInfo());

        return response;
    }

    private static List<Long> getRandomSubSubcategories(Integer n) {
        URI uri = UriBuilder.fromUri(BASE_PATH + SUBSUBCATEGORIES_PATH)
                .queryParam("withQuizzes", true)
                .queryParam("n", n).build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();
        checkIfError(response.getStatusInfo());

        Gson gson = new Gson();
        SubSubIds[] array = gson.fromJson(response.readEntity(String.class), SubSubIds[].class);

        return Arrays.stream(array)
                .map(a -> a.id)
                .collect(Collectors.toList());
    }

    private static void checkIfError(Response.StatusType status) {
        if (status.getFamily().equals(Response.Status.Family.CLIENT_ERROR)) {
            throw new ClientErrorException(status.getStatusCode());
        }
        if (status.getFamily().equals(Response.Status.Family.SERVER_ERROR)) {
            throw new ServerErrorException(status.getStatusCode());
        }
    }

    private static class SubSubIds {
        Long id;
    }
}
