package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
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
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));
    }
}
