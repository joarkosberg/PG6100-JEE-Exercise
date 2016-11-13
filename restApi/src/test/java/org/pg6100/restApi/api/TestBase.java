package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pg6100.restApi.api.util.JBossUtil;
import org.pg6100.restApi.dto.CategoryDto;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class TestBase {
    protected final static String questionRest = "/quiz/api/quizzes";
    protected final static String subSubCategoryRest = "/quiz/api/quizzes";
    protected final static String subCategoryRest = "/quiz/api/subsubcategories";
    protected final static String categoryRest = "quiz/api/categories";

    @BeforeClass
    public static void initClass() {
        JBossUtil.waitForJBoss(10);

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Before
    @After
    public void clean() {
        List<CategoryDto> categories = Arrays.asList(given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract().as(CategoryDto[].class));


        categories.stream().forEach(dto ->
                given().pathParam("id", dto.id)
                        .delete("/id/{id}")
                        .then().statusCode(204));

        get().then().statusCode(200).body("size()", is(0));
    }
}