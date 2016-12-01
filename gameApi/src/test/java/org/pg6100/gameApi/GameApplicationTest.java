package org.pg6100.gameApi;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.restassured.http.ContentType;
import org.junit.ClassRule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.given;

public class GameApplicationTest extends GameApplicationTestBase {

    private static final Long[] questions = new Long[]{Long.valueOf(1), Long.valueOf(2),
            Long.valueOf(3), Long.valueOf(4)};

    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class, "config.yml");

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

    private String getAMockedJsonResponse(double usd, double eur, double gbp) {
        String json = "{";
        json += "\"id\": \"\" , ";
        json += "\"question\": \"Hva sier kua?\" , ";

        json += "\"answers\": [";
        json += "\"Baaa\" , ";
        json += "\"Miaow\" , ";
        json += "\"Moo\" , ";
        json += "\"Hi\" , ";
        json += "], ";

        json += "\"correctAnswer\": 2 , ";

        json += "\"subSubCategory\": {";
        json += "\"id\": \"4\" , ";
        json += "\"name\": \"Dyr\" , ";
        json += "\"subCategory\": {";

        json += "\"id\": \"3\" , ";
        json += "\"name\": \"Levende vesen\" , ";
        json += "\"category\": {";

        json += "\"id\": \"2\" , ";
        json += "\"name\": \"Alt\"";

        json += "}";
        json += "}";
        json += "}";
        json += "}";
        return json;
        /*
        {
  "id": "",
  "question": "string",
  "answers": [
    "string",
    "string",
    "string",
    "string"
  ],
  "correctAnswer": 1,
  "subSubCategory": {
    "id": "4",
    "name": "string",
    "subCategory": {
      "id": "string",
      "name": "string",
      "category": {
        "id": "string",
        "name": "string"
      }
    }
  }
}
         */
    }

    private void stubJsonResponse(String json) throws Exception {

        /*
            here we are instructing WireMock to return the given json
            every time there is a request for

            /latest?base=NOK

         */

        wiremockServer.stubFor( //prepare a stubbed response for the given request
                WireMock.get( //define the GET request to mock
                        urlMatching("/latest.*"))
                        .withQueryParam("base", WireMock.matching("NOK"))
                        // define the mocked response of the GET
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.getBytes("utf-8").length)
                                .withBody(json)));
    }
}
