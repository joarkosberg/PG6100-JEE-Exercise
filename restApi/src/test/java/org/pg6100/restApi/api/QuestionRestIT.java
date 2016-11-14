package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.pg6100.quiz.entity.SubSubCategory;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.QuestionDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class QuestionRestIT extends TestBase {

    @Before
    public void setBasePath(){ // Used to change after each clean between methods
        RestAssured.basePath = questionRest;
    }

    @Test
    public void testCreateAndGetQuestion() {
        CategoryDto category = new CategoryDto(null, "cat");
        category.id = createCategory(category, questionRest);

        SubCategoryDto subCategory = new SubCategoryDto(null, "sub", category);
        subCategory.id = createSubCategory(subCategory, questionRest);

        SubSubCategoryDto subSubCategory = new SubSubCategoryDto(null, "subsub", subCategory);
        subSubCategory.id = createSubSubCategory(subSubCategory, questionRest);
        System.out.println(subCategory.id);

        get().then().statusCode(200).body("size()", is(0));
        given().contentType(ContentType.JSON)
                .body(new QuestionDto(null, "question", answers, 3, subSubCategory))
                .post()
                .then()
                .statusCode(200)
                .extract();
        get().then().statusCode(200).body("size()", is(1));
    }
}
