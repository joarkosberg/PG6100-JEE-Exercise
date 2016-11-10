package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.quiz.entity.Category;
import org.pg6100.restApi.dto.CategoryDto;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class CategoryRestIT extends CategoryRestTestBase {

    @BeforeClass
    public static void before(){
        RestAssured.basePath = "/quiz/api/categories";
    }

    @Test
    public void testCreateAndGet() {
        String category = "c1";
        CategoryDto dto = new CategoryDto(category);

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
    public void testUpdate() throws Exception {
        //first create with a POST
        String id = given().contentType(ContentType.JSON)
                .body(new CategoryDto("cat"))
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        //check if POST was fine
        get("/id/" + id).then().body("name", is(id));

        String updatedText = "new updated categoryname";

        //now change text with PUT
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(new CategoryDto(updatedText))
                .put("/id/{id}")
                .then()
                .statusCode(204);

        //was the PUT fine?
        get("/id/" + id).then().body("name", is(updatedText));
    }
}
