package org.pg6100.gameApi;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pg6100.gameApi.model.Game;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public abstract class GameApplicationTestBase {

    protected static WireMockServer wiremockServer;
    protected static final Integer[] ANSWERS = new Integer []{0, 2, 1, 3};
    private static final String FILE_PATH = "src/test/java/org/pg6100/gameApi/json/";

    @BeforeClass
    public static void initClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/game/api/games";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        wiremockServer = new WireMockServer(
                wireMockConfig().port(8080).notifier(new ConsoleNotifier(true))
        );
        wiremockServer.start();

        try {
            stubJsonRandomQuizResponse(getJsonData("randomQuestions"));
            stubJsonSubSubCategoryResponse(getJsonData("subSubCategory"));
            stubJsonQuizzesResponse();
        } catch (FileNotFoundException e){
            System.out.println("\nFile not found: " + e.getMessage() + "\n");
        } catch (UnsupportedEncodingException e){
            System.out.println("\nEncoding not supported: " + e.getMessage() + "\n");
        }
    }

    @AfterClass
    public static void tearDown(){
        wiremockServer.stop();
    }

    private static void stubJsonRandomQuizResponse(String json) throws UnsupportedEncodingException {
        wiremockServer.stubFor(
                post(urlEqualTo("/quiz/api/randomquizzes?n=4&filter=5"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.getBytes("utf-8").length)
                                .withStatus(201)
                                .withBody(json)
                        ));
    }

    private static void stubJsonSubSubCategoryResponse(String json) throws UnsupportedEncodingException {
        wiremockServer.stubFor(
                get(urlEqualTo("/quiz/api/subsubcategories?withQuizzes=true&n=4"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.getBytes("utf-8").length)
                                .withStatus(200)
                                .withBody(json)));
    }

    private static void stubJsonQuizzesResponse() throws UnsupportedEncodingException, FileNotFoundException {
        String question1 = getJsonData("questions/question1");
        String question2 = getJsonData("questions/question2");
        String question3 = getJsonData("questions/question3");
        String question4 = getJsonData("questions/question4");

        wiremockServer.stubFor(
                get(urlEqualTo("/quiz/api/quizzes/1"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + question1.getBytes("utf-8").length)
                                .withBody(question1)));
        wiremockServer.stubFor(
                get(urlEqualTo("/quiz/api/quizzes/2"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + question2.getBytes("utf-8").length)
                                .withBody(question2)));
        wiremockServer.stubFor(
                get(urlEqualTo("/quiz/api/quizzes/3"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + question3.getBytes("utf-8").length)
                                .withBody(question3)));
        wiremockServer.stubFor(
                get(urlEqualTo("/quiz/api/quizzes/4"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + question4.getBytes("utf-8").length)
                                .withBody(question4)));
    }

    private static String getJsonData(String jsonFile) throws FileNotFoundException {
        String data = "";
        try (Scanner in = new Scanner(new File(FILE_PATH + jsonFile + ".json"))) {
            while (in.hasNextLine()) {
                data += in.nextLine() + "\n";
            }
        }
        return data;
    }

    @Before
    @After
    public void clean() {
        List<Game> list = Arrays.asList(given().accept(ContentType.JSON)
                .queryParam("n", 100)
                .get()
                .then()
                .statusCode(200)
                .extract().as(Game[].class));

        list.forEach(game ->
                given().pathParam("id", game.getId())
                        .delete("/{id}")
                        .then().statusCode(204));

        io.restassured.RestAssured.get().then().statusCode(200).body("size()", is(0));
    }

    public String createGame(){
        return given().contentType(ContentType.JSON)
                .queryParam("n", 4)
                .post()
                .then()
                .statusCode(201)
                .extract().header("location");
    }
}
