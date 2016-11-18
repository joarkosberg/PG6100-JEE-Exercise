package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.post;

public class RandomQuizzesRestIT extends TestBase {

    @BeforeClass
    public static void setBasePath(){
        activeRest = randomQuizzesRest;
        RestAssured.basePath = activeRest;
    }

    @Test
    public void testRandomQuizzesWithValidCategories(){

    }


    @Test
    public void testRandomQuizzesWithoutParameter(){

    }


    @Test
    public void testNoQuestions(){
        post().then()
                .statusCode(400);
    }


    @Test
    public void testInvalidIdOfAnyCategory(){

    }
}
