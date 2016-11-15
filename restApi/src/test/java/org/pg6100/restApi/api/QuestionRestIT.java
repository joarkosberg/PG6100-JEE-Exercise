package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.quiz.entity.Question;
import org.pg6100.quiz.entity.SubSubCategory;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.QuestionDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class QuestionRestIT extends TestBase {

    @BeforeClass
    public static void setActivePath(){
        activeRest = questionRest;
        RestAssured.basePath = activeRest;
    }

    @Test
    public void testCreateAndGetQuestion() {
        SubSubCategoryDto subSubCategoryDto = createAndGetSubSubCategory();

        get().then().statusCode(200).body("size()", is(0));
        given().contentType(ContentType.JSON)
                .body(new QuestionDto(null, "question", answers, 3, subSubCategoryDto))
                .post()
                .then()
                .statusCode(200)
                .extract();
        get().then().statusCode(200).body("size()", is(1));
    }

    @Test
    public void testUpdateQuestion(){
        SubSubCategoryDto subSubCategoryDto = createAndGetSubSubCategory();

        int answer = 3;
        String question = "Question";
        QuestionDto questionDto = new QuestionDto(null, question, answers, answer, subSubCategoryDto);
        questionDto.id = createQuestion(questionDto);

        get("/id/" + questionDto.id)
                .then()
                .body("correctAnswer", is(answer));
        get("/id/" + questionDto.id)
                .then()
                .statusCode(200)
                .body("question", is(question));

        int newAnswer = 1;
        String newQuestion = "NewQuestion";
        questionDto.correctAnswer = newAnswer;
        questionDto.question = newQuestion;
        given().contentType(ContentType.JSON)
                .pathParam("id", questionDto.id)
                .body(questionDto)
                .put("/id/{id}")
                .then()
                .statusCode(204);

        get("/id/" + questionDto.id)
                .then()
                .body("correctAnswer", is(newAnswer));
        get("/id/" + questionDto.id)
                .then()
                .body("question", is(newQuestion));
    }

    @Test
    public void testPatchQuestionText(){
        SubSubCategoryDto subSubCategoryDto = createAndGetSubSubCategory();

        String question = "Question";
        QuestionDto questionDto = new QuestionDto(null, question, answers, 3, subSubCategoryDto);
        questionDto.id = createQuestion(questionDto);

        get("/id/" + questionDto.id)
                .then()
                .body("question", is(question));

        String newQuestion = "NewQuestion";
        given().contentType(ContentType.TEXT)
                .pathParam("id", questionDto.id)
                .body(newQuestion)
                .patch("/id/{id}")
                .then()
                .statusCode(204);

        get("/id/" + questionDto.id)
                .then()
                .body("question", is(newQuestion));
    }



    //TODO test invalid requests.
    //TODO MORE THAN ONE



    public SubSubCategoryDto createAndGetSubSubCategory(){
        CategoryDto category = new CategoryDto(null, "cat");
        category.id = createCategory(category);

        SubCategoryDto subCategory = new SubCategoryDto(null, "sub", category);
        subCategory.id = createSubCategory(subCategory);

        SubSubCategoryDto subSubCategory = new SubSubCategoryDto(null, "subsub", subCategory);
        subSubCategory.id = createSubSubCategory(subSubCategory);
        return subSubCategory;
    }
}
