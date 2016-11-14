package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class SubCategoryRestIT extends TestBase {

    @BeforeClass
    public static void setBasePath(){
        activeRest = subCategoryRest;
        RestAssured.basePath = activeRest;
    }

    @Test
    public void testCreateAndGetSubCategory() {
        get().then().statusCode(200).body("size()", is(0));

        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        given().contentType(ContentType.JSON)
                .body(new SubCategoryDto(null, "sub", categoryDto))
                .post()
                .then()
                .statusCode(200)
                .extract();

        get().then().statusCode(200).body("size()", is(1));
    }

}
