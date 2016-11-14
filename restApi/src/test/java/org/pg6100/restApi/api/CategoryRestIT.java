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

    @BeforeClass
    public static void setBasePath(){
        activeRest = categoryRest;
        RestAssured.basePath = activeRest;
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
        String id = createCategory(new CategoryDto(null, text));

        //check if POST was fine
        get("/id/" + id).then().body("name", is(text));

        //now change text with PUT
        String updatedText = "new updated categoryname";
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

    //Patch category


    //@Path("/withQuizzes")



    //@Path("/withQuizzes/subsubcategories")



    //@Path("/id/{id}/subcategories")
}
