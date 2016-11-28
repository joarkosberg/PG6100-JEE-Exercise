package org.pg6100.quizApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.quizApi.dto.CategoryDto;
import org.pg6100.quizApi.dto.QuestionDto;
import org.pg6100.quizApi.dto.SubCategoryDto;
import org.pg6100.quizApi.dto.SubSubCategoryDto;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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

        get().then()
                .statusCode(200)
                .body("size()", is(0));

        given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract();

        get().then()
                .statusCode(200)
                .body("size()", is(1));

        createCategory(new CategoryDto(null, "c2"));

        get().then()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    public void testUpdateCategory() {
        //first create with a POST
        String text = "cat";
        String id = createCategory(new CategoryDto(null, text));

        //check if POST was fine
        get("/id/" + id)
                .then()
                .body("name", is(text));

        //now change text with PUT
        String updatedText = "new updated categoryname";
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(new CategoryDto(id, updatedText))
                .put("/id/{id}")
                .then()
                .statusCode(301);

        /*
        //was the PUT fine?
        get().then()
                .statusCode(200)
                .body("size()", is(1));
        get("/id/" + id)
                .then()
                .body("name", is(updatedText));
                */
    }

    //Patch category
    @Test
    public void testPatchCategoryName() {
        //Create
        String categoryName = "cat";
        CategoryDto categoryDto = new CategoryDto(null, categoryName);
        categoryDto.id = createCategory(categoryDto);

        get("/id/" + categoryDto.id)
                .then()
                .body("name", is(categoryName));

        //Patch
        String newName = "newCat";
        given().contentType(ContentType.TEXT)
                .pathParam("id", categoryDto.id)
                .body(newName)
                .patch("/id/{id}")
                .then()
                .statusCode(301);

        /*
        get("/id/" + categoryDto.id)
                .then()
                .body("name", is(newName));
                */
    }

    @Test //TODO WAY TOO LONG TEST/METHOD (All tests under this one)
    public void testGetCategoriesWithQuizzes(){
        CategoryDto category1 = new CategoryDto(null, "cat1");
        category1.id = createCategory(category1);
        CategoryDto category2 = new CategoryDto(null, "cat2");
        category2.id = createCategory(category2);
        CategoryDto category3 = new CategoryDto(null, "cat3");
        category3.id = createCategory(category3);

        SubCategoryDto subCategory1 = new SubCategoryDto(null, "sub1", category1);
        subCategory1.id = createSubCategory(subCategory1);
        SubCategoryDto subCategory2 = new SubCategoryDto(null, "sub2", category2);
        subCategory2.id = createSubCategory(subCategory2);
        SubCategoryDto subCategory3 = new SubCategoryDto(null, "sub3", category3);
        subCategory3.id = createSubCategory(subCategory3);

        SubSubCategoryDto subSubCategory1 = new SubSubCategoryDto(null, "subsub1", subCategory1);
        subSubCategory1.id = createSubSubCategory(subSubCategory1);
        SubSubCategoryDto subSubCategory2 = new SubSubCategoryDto(null, "subsub2", subCategory1);
        subSubCategory2.id = createSubSubCategory(subSubCategory2);
        SubSubCategoryDto subSubCategory3 = new SubSubCategoryDto(null, "subsub3", subCategory2);
        subSubCategory3.id = createSubSubCategory(subSubCategory3);
        SubSubCategoryDto subSubCategory4 = new SubSubCategoryDto(null, "subsub4", subCategory3);
        subSubCategory4.id = createSubSubCategory(subSubCategory4);

        QuestionDto question1 = new QuestionDto(null, "question", answers, 3, subSubCategory1);
        question1.id = createQuestion(question1);
        QuestionDto question2 = new QuestionDto(null, "question", answers, 3, subSubCategory2);
        question2.id = createQuestion(question2);
        QuestionDto question3 = new QuestionDto(null, "question", answers, 3, subSubCategory4);
        question3.id = createQuestion(question3);

        //Categories counted
        get().then()
                .statusCode(200)
                .body("size()", is(3));

        //Categories with quizzes counted
        get("/withquizzes")
                .then()
                .statusCode(200)
                .body("size()", is(2));

        List<CategoryDto> categories = Arrays.asList(given().accept(ContentType.JSON)
                .get("/withquizzes")
                .then()
                .statusCode(200)
                .extract().as(CategoryDto[].class));

        assertTrue(categories.stream().anyMatch(c -> c.id.equals(category1.id)));
        assertFalse(categories.stream().anyMatch(c -> c.id.equals(category2.id)));
    }

    @Test
    public void testGetSubSubCategoriesWithQuizzes(){
        CategoryDto category1 = new CategoryDto(null, "cat1");
        category1.id = createCategory(category1);
        CategoryDto category2 = new CategoryDto(null, "cat2");
        category2.id = createCategory(category2);
        CategoryDto category3 = new CategoryDto(null, "cat3");
        category3.id = createCategory(category3);

        SubCategoryDto subCategory1 = new SubCategoryDto(null, "sub1", category1);
        subCategory1.id = createSubCategory(subCategory1);
        SubCategoryDto subCategory2 = new SubCategoryDto(null, "sub2", category2);
        subCategory2.id = createSubCategory(subCategory2);
        SubCategoryDto subCategory3 = new SubCategoryDto(null, "sub3", category3);
        subCategory3.id = createSubCategory(subCategory3);

        SubSubCategoryDto subSubCategory1 = new SubSubCategoryDto(null, "subsub1", subCategory1);
        subSubCategory1.id = createSubSubCategory(subSubCategory1);
        SubSubCategoryDto subSubCategory2 = new SubSubCategoryDto(null, "subsub2", subCategory1);
        subSubCategory2.id = createSubSubCategory(subSubCategory2);
        SubSubCategoryDto subSubCategory3 = new SubSubCategoryDto(null, "subsub3", subCategory2);
        subSubCategory3.id = createSubSubCategory(subSubCategory3);
        SubSubCategoryDto subSubCategory4 = new SubSubCategoryDto(null, "subsub4", subCategory3);
        subSubCategory4.id = createSubSubCategory(subSubCategory4);

        QuestionDto question1 = new QuestionDto(null, "question", answers, 3, subSubCategory1);
        question1.id = createQuestion(question1);
        QuestionDto question2 = new QuestionDto(null, "question", answers, 3, subSubCategory1);
        question2.id = createQuestion(question2);
        QuestionDto question3 = new QuestionDto(null, "question", answers, 3, subSubCategory4);
        question3.id = createQuestion(question3);

        get("/withquizzes/subsubcategories")
                .then()
                .statusCode(200)
                .body("size()", is(2));

        List<SubSubCategoryDto> subSubCategories = Arrays.asList(given().accept(ContentType.JSON)
                .get("/withquizzes/subsubcategories")
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDto[].class));

        assertTrue(subSubCategories.stream().anyMatch(c -> c.id.equals(subSubCategory4.id)));
        assertFalse(subSubCategories.stream().anyMatch(c -> c.id.equals(subSubCategory2.id)));
    }

    @Test
    public void testGetAllSubCategoriesOfACategory(){
        CategoryDto category1 = new CategoryDto(null, "cat1");
        category1.id = createCategory(category1);
        CategoryDto category2 = new CategoryDto(null, "cat2");
        category2.id = createCategory(category2);
        CategoryDto category3 = new CategoryDto(null, "cat3");
        category3.id = createCategory(category3);

        SubCategoryDto subCategory1 = new SubCategoryDto(null, "sub1", category1);
        subCategory1.id = createSubCategory(subCategory1);
        SubCategoryDto subCategory2 = new SubCategoryDto(null, "sub2", category1);
        subCategory2.id = createSubCategory(subCategory2);
        SubCategoryDto subCategory3 = new SubCategoryDto(null, "sub3", category3);
        subCategory3.id = createSubCategory(subCategory3);

        //Sub categories counted
        get("/id/" + category1.id + "/subcategories")
                .then()
                .body("size()", is(2));

        given().pathParam("id", category2.id)
                .get("/id/{id}/subcategories")
                .then()
                .body("size()", is(0));

        given().pathParam("id", category3.id)
                .get("/id/{id}/subcategories")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    //Invalid requests 4**
    @Test
    public void testCreateWithSpecifiedId(){
        given().contentType(ContentType.JSON)
                .body(new CategoryDto("1", "name"))
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateWithoutName(){
        given().contentType(ContentType.JSON)
                .body(new CategoryDto(null, null))
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetInvalidCategory(){
        get("/id/" + "1")
                .then()
                .statusCode(404);
    }
}