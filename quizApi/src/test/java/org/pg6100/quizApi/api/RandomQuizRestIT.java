package org.pg6100.quizApi.api;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.quizApi.dto.CategoryDto;
import org.pg6100.quizApi.dto.QuestionDto;
import org.pg6100.quizApi.dto.SubCategoryDto;
import org.pg6100.quizApi.dto.SubSubCategoryDto;

import io.restassured.response.Response;
import java.util.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.Is.is;

public class RandomQuizRestIT extends TestBase{

    @BeforeClass
    public static void setBasePath(){
        activeRest = randomQuizRest;
        RestAssured.basePath = activeRest;
    }

    @Test
    public void testRandomQuizWithValidCategories(){
        CategoryDto category1 = new CategoryDto(null, "cat1");
        category1.id = createCategory(category1);
        CategoryDto category2 = new CategoryDto(null, "cat2");
        category2.id = createCategory(category2);

        SubCategoryDto subCategory1 = new SubCategoryDto(null, "sub1", category1);
        subCategory1.id = createSubCategory(subCategory1);
        SubCategoryDto subCategory2 = new SubCategoryDto(null, "sub2", category2);
        subCategory2.id = createSubCategory(subCategory2);

        SubSubCategoryDto subSubCategory1 = new SubSubCategoryDto(null, "subsub1", subCategory1);
        subSubCategory1.id = createSubSubCategory(subSubCategory1);
        SubSubCategoryDto subSubCategory2 = new SubSubCategoryDto(null, "subsub2", subCategory1);
        subSubCategory2.id = createSubSubCategory(subSubCategory2);
        SubSubCategoryDto subSubCategory3 = new SubSubCategoryDto(null, "subsub3", subCategory2);
        subSubCategory3.id = createSubSubCategory(subSubCategory3);

        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question2", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question3", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));

        given().queryParam("filter", subSubCategory2.id)
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", subSubCategory1.id)
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", subCategory1.id)
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", subCategory2.id)
                .get()
                .then()
                .statusCode(409);

        given().queryParam("filter", category1.id)
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", category2.id)
                .get()
                .then()
                .statusCode(409);
    }

    @Test
    public void testRandomQuizWithoutParameterAlwaysGetsAQuestion(){
        createTestData();

        Set<Long> ids = new HashSet<>();
        for(int i = 0; i < 20; i++) {
            Response response = get().then()
                    .statusCode(200)
                    .extract().response();
            ids.add(Long.valueOf(response.body().path("id")));
        }

        System.out.println(ids.size() + " - IDS SIZE");
        assertTrue(ids.size() > 2); // Assert More than two question gets picked (Random)
        //Kan feile. Men sukssess rate > 95%
    }

    @Test
    public void testRandomQuizInvalidIdTest(){
        List<Long> ids = createTestData();
        Random random = new Random();
        Long randomNumber;

        while(true){
            randomNumber = random.nextLong();
            if(!ids.contains(randomNumber)) break;
        }

        given().queryParam("filter", randomNumber)
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    public void randomQuizWithoutQuestionsTest(){
        get().then()
                .statusCode(400);
    }

    public List<Long> createTestData(){
        CategoryDto category1 = new CategoryDto(null, "cat1");
        category1.id = createCategory(category1);
        CategoryDto category2 = new CategoryDto(null, "cat2");
        category2.id = createCategory(category2);

        SubCategoryDto subCategory1 = new SubCategoryDto(null, "sub1", category1);
        subCategory1.id = createSubCategory(subCategory1);
        SubCategoryDto subCategory2 = new SubCategoryDto(null, "sub2", category2);
        subCategory2.id = createSubCategory(subCategory2);

        SubSubCategoryDto subSubCategory1 = new SubSubCategoryDto(null, "subsub1", subCategory1);
        subSubCategory1.id = createSubSubCategory(subSubCategory1);
        SubSubCategoryDto subSubCategory2 = new SubSubCategoryDto(null, "subsub2", subCategory1);
        subSubCategory2.id = createSubSubCategory(subSubCategory2);
        SubSubCategoryDto subSubCategory3 = new SubSubCategoryDto(null, "subsub3", subCategory2);
        subSubCategory3.id = createSubSubCategory(subSubCategory3);

        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question2", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question3", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));

        List<Long> ids = new ArrayList<>();
        ids.add(Long.valueOf(subCategory1.id));
        ids.add(Long.valueOf(subCategory2.id));
        ids.add(Long.valueOf(category1.id));
        ids.add(Long.valueOf(category2.id));
        ids.add(Long.valueOf(subSubCategory1.id));
        ids.add(Long.valueOf(subSubCategory2.id));
        ids.add(Long.valueOf(subSubCategory3.id));
        return ids;
    }
}
