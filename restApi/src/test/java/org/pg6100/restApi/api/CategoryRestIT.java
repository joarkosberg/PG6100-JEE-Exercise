package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.restApi.dto.CategoryDto;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class CategoryRestIT extends TestBase {

    @Before
    public void setBasePath(){ // Used to change after each clean between methods
        RestAssured.basePath = categoryRest;
    }

    @Test
    public void testCreateAndGetCategory() {
        String category = "c1";
        CategoryDto dto = new CategoryDto(null, category);

        get().then().statusCode(200).body("size()", is(0));

        given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract();

        get().then().statusCode(200).body("size()", is(1));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        //first create with a POST
        String text = "cat";
        String id = given().contentType(ContentType.JSON)
                .body(new CategoryDto(null, text))
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        //check if POST was fine
        get("/id/" + id).then().body("name", is(text));

        String updatedText = "new updated categoryname";

        //now change text with PUT
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(new CategoryDto(id, updatedText))
                .put("/id/{id}")
                .then()
                .statusCode(204);

        //was the PUT fine?
        get().then().statusCode(200).body("size()", is(1));
        get("/id/" + id).then().body("name", is(updatedText));
    }
}
