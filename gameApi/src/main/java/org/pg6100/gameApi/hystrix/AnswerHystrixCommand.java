package org.pg6100.gameApi.hystrix;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class AnswerHystrixCommand extends HystrixCommand<Integer> {

    private static final String QUIZZES_PATH = "/quizzes";
    private static final String BASE_PATH = "http://localhost:8080/quiz/api";
    private final Long questionId;

    public AnswerHystrixCommand(Long questionId) {
        super(HystrixCommandGroupKey.Factory.asKey("Answer"));
        this.questionId = questionId;
    }

    @Override
    public Integer run() throws Exception {
        URI uri = UriBuilder.fromUri(BASE_PATH + QUIZZES_PATH + "/" + questionId.toString())
                .build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();

        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject) parser.parse(response.readEntity(String.class));

        Integer answer = json.get("correctAnswer").getAsInt();
        return answer;
    }

    @Override
    protected Integer getFallback(){
        return null;
    }
}
