package org.pg6100.quizApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pg6100.quizApi.api.util.JBossUtil;
import org.pg6100.quizApi.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class TestBase {
    protected final static String questionRest = "/quiz/api/quizzes";
    protected final static String subSubCategoryRest = "/quiz/api/subsubcategories";
    protected final static String subCategoryRest = "/quiz/api/subcategories";
    protected final static String categoryRest = "/quiz/api/categories";
    protected final static String randomQuizRest = "/quiz/api/randomquiz";
    protected final static String randomQuizzesRest = "/quiz/api/randomquizzes";

    protected static String activeRest;

    protected List<String> answers = Arrays.asList("a1", "a2", "a3", "a4");

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
        //Question
        RestAssured.basePath = questionRest;
        List<QuestionDto> questions = Arrays.asList(given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract().as(QuestionDto[].class));
        questions.stream().forEach(dto ->
                given().pathParam("id", dto.id)
                        .delete("/{id}")
                        .then().statusCode(204));
        get().then().statusCode(200).body("size()", is(0));

        //Sub Sub category
        RestAssured.basePath = subSubCategoryRest;
        List<SubSubCategoryDto> subSubCategories = Arrays.asList(given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDto[].class));
        subSubCategories.stream().forEach(subSub ->
                given().pathParam("id", subSub.id)
                        .delete("/{id}")
                        .then().statusCode(204));
        get().then().statusCode(200).body("size()", is(0));

        //Sub category
        RestAssured.basePath = subCategoryRest;
        ListDto<?> subCategories = given()
                .accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .as(ListDto.class);
        subCategories.list.stream().map(c -> ((Map) c).get("id"))
                .forEach(id ->
                        given().pathParam("id", id)
                                .delete("/{id}")
                                .then().statusCode(204));
        get().then().statusCode(200).body("list.size()", is(0));

        //Category
        RestAssured.basePath = categoryRest;
        ListDto<?> categories = given()
                .accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .as(ListDto.class);
        categories.list.stream().map(c -> ((Map) c).get("id"))
                .forEach(id ->
                given().pathParam("id", id)
                        .delete("/{id}")
                        .then().statusCode(204));
        get().then().statusCode(200).body("list.size()", is(0));

        RestAssured.basePath = activeRest;
    }

    public String createCategory(CategoryDto categoryDto){
        RestAssured.basePath = categoryRest;
        String id = given().contentType(ContentType.JSON)
                .body(categoryDto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();
        RestAssured.basePath = activeRest;
        return id;
    }

    public String createSubCategory(SubCategoryDto subCategoryDto){
        RestAssured.basePath = subCategoryRest;
        String id = given().contentType(ContentType.JSON)
                .body(subCategoryDto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();
        RestAssured.basePath = activeRest;
        return id;
    }

    public String createSubSubCategory(SubSubCategoryDto subSubCategoryDto){
        RestAssured.basePath = subSubCategoryRest;
        String id = given().contentType(ContentType.JSON)
                .body(subSubCategoryDto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();
        RestAssured.basePath = activeRest;
        return id;
    }

    public String createQuestion(QuestionDto questionDto){
        RestAssured.basePath = questionRest;
        String id = given().contentType(ContentType.JSON)
                .body(questionDto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();
        RestAssured.basePath = activeRest;
        return id;
    }
}
