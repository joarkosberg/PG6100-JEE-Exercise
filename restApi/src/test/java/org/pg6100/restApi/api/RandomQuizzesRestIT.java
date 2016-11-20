package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.QuestionDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.core.Is.is;

public class RandomQuizzesRestIT extends TestBase {

    @BeforeClass
    public static void setBasePath(){
        activeRest = randomQuizzesRest;
        RestAssured.basePath = activeRest;
    }

    @Test
    public void testRandomQuizzesWithValidCategories(){
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
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));

        createQuestion(new QuestionDto(null, "question2", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question3", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));

        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory3));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory3));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory3));

        given().queryParam("filter", subSubCategory2.id)
                .post()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", subSubCategory1.id)
                .post()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", subCategory1.id)
                .post()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", subCategory2.id)
                .post()
                .then()
                .statusCode(400);

        given().queryParam("filter", category1.id)
                .post()
                .then()
                .statusCode(200)
                .body("size()", is(5));

        given().queryParam("filter", category2.id)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void testRandomQuizzesWithoutParameterWithDifferentSizes(){
        createTestData();

        for(int i = 0; i < 10; i++){
            given().queryParam("n", i)
                    .then()
                    .statusCode(200)
                    .body("size()", is(i));
        }
    }

    @Test
    public void testNoQuestions(){
        post().then()
                .statusCode(400);
    }

    @Test
    public void testInvalidIdOfAnyCategory(){
        List<Long> ids = createTestData();
        Random random = new Random();
        Long randomId;

        while(true){
            randomId = random.nextLong();
            if(!ids.contains(randomId)) break;
        }

        given().queryParam("filter", randomId)
                .post()
                .then()
                .statusCode(404);
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
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));
        createQuestion(new QuestionDto(null, "question1", answers, 3, subSubCategory1));

        createQuestion(new QuestionDto(null, "question2", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question3", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory2));

        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory3));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory3));
        createQuestion(new QuestionDto(null, "question4", answers, 3, subSubCategory3));

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
