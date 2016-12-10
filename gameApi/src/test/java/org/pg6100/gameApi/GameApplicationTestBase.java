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
                                .withBody(json)
                        ));
    }

    private static void stubJsonSubSubCategoryResponse(String json) throws UnsupportedEncodingException {
        wiremockServer.stubFor(
                get(urlEqualTo("/quiz/api/subsubcategories?withQuizzes=true&n=4"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.getBytes("utf-8").length)
                                .withBody(json)));
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
}
