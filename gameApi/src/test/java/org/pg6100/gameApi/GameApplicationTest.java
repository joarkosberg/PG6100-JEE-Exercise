package org.pg6100.gameApi;

import io.dropwizard.testing.junit.DropwizardAppRule;
import io.restassured.http.ContentType;
import org.junit.ClassRule;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.core.Is.is;

public class GameApplicationTest extends GameApplicationTestBase {

    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class, "config.yml");

    @Test
    public void testCreateGameWithNegativeNumberOfQuizzesFails() {
        get().then().statusCode(200).body("size()", is(0));

        given().contentType(ContentType.JSON)
                .queryParam("n", -2)
                .post()
                .then()
                .statusCode(400);

        get().then().statusCode(200).body("size()", is(0));
    }

    @Test
    public void testCreateGame() throws Exception {
       String url = given().contentType(ContentType.JSON)
                .queryParam("n", 4)
                .post()
                .then()
                .statusCode(201)
                .extract().header("location");

       given().contentType(ContentType.JSON)
                .get(url)
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    public void testSwagger(){
        given().baseUri("http://localhost")
                .basePath("/game/api")
                .port(8081)
                .accept(ContentType.JSON)
                .get("swagger.json")
                .then()
                .statusCode(200);
    }
}
