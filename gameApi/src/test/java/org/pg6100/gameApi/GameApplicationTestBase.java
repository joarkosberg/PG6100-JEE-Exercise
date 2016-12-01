package org.pg6100.gameApi;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pg6100.gameApi.model.Game;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public abstract class GameApplicationTestBase {

    protected static WireMockServer wiremockServer;

    @BeforeClass
    public static void initClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/game/api/games";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        wiremockServer = new WireMockServer(
                wireMockConfig().port(8099).notifier(new ConsoleNotifier(true))
        );
        wiremockServer.start();
    }

    @AfterClass
    public static void tearDown(){
        wiremockServer.stop();
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

        get().then().statusCode(200).body("size()", is(0));
    }
}
