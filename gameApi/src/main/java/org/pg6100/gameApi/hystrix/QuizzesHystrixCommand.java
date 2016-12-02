package org.pg6100.gameApi.hystrix;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuizzesHystrixCommand extends HystrixCommand<Long []>{
    private static final String BASE_PATH = "http://localhost:8080/quiz/api";
    private static final String RANDOMQUIZZES_PATH = "/randomquizzes";
    private static final String SUBSUBCATEGORIES_PATH = "/subsubcategories";

    private final Integer n;

    public QuizzesHystrixCommand(Integer n) {
        super(HystrixCommandGroupKey.Factory.asKey("Quizzes"));
        this.n = n;
    }

    @Override
    public Long[] run() throws Exception {
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

    private Response getQuizzes(Integer limit, Long category) {
        URI uri = UriBuilder.fromUri(BASE_PATH + RANDOMQUIZZES_PATH)
                .queryParam("n", limit)
                .queryParam("filter", category)
                .build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).post(null);

        return response;
    }

    private List<Long> getRandomSubSubcategories(Integer n) {
        URI uri = UriBuilder.fromUri(BASE_PATH + SUBSUBCATEGORIES_PATH +
                "?withQuizzes=true&n=" + n)
                .build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();

        Gson gson = new Gson();
        SubSubIds[] array = gson.fromJson(response.readEntity(String.class), SubSubIds[].class);

        return Arrays.stream(array)
                .map(a -> a.id)
                .collect(Collectors.toList());
    }

    private class SubSubIds {
        Long id;
    }

    @Override
    protected Long[] getFallback(){
        return null;
        //throw new WebApplicationException("Could'nt retrieve quizzes, quiz service down. " +
                //"Please try again shortly!", 503);
    }
}
