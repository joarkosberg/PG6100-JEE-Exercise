package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.pg6100.quiz.entity.SubSubCategory;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.QuestionDto;
import org.pg6100.restApi.dto.SubCategoryDto;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class QuestionRestIT extends TestBase {

    @Before
    public void setBasePath(){ // Used to change after each clean between methods
        RestAssured.basePath = questionRest;
    }

    @Test
    public void testCreateAndGetCategory() {
        CategoryDto category = new CategoryDto(null, "cat");
        String categoryId = createCategory(category, questionRest);
        String subCategoryId = createSubCategory(new SubCategoryDto(), questionRest);
        String subSubCategoryId = createSubSubCategory("ssCat", subCategoryId, questionRest);

        get().then().statusCode(200).body("size()", is(0));

        given().contentType(ContentType.JSON)
                .body(new QuestionDto(null, "question", answers, 3, new SubSubCategory()))
                .post()
                .then()
                .statusCode(200)
                .extract();
        get().then().statusCode(200).body("size()", is(1));
    }
}
