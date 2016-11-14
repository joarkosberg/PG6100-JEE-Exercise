package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class SubSubCategoryRestIT extends TestBase {

    @BeforeClass
    public static void setBasePath(){
        activeRest = subSubCategoryRest;
        RestAssured.basePath = activeRest;
    }

    @Test
    public void testCreateAndGetSubSubCategory() {
        get().then().statusCode(200).body("size()", is(0));

        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto.id = createSubCategory(subCategoryDto);

        given().contentType(ContentType.JSON)
                .body(new SubSubCategoryDto(null, "subsub", subCategoryDto))
                .post()
                .then()
                .statusCode(200)
                .extract();

        get().then().statusCode(200).body("size()", is(1));
    }


    //TODO PUT

    //TODO PATCH

    //TODO @Path("/parent/{id}")


    //TODO test invalid requests.
    //TODO MORE THAN ONE


}
