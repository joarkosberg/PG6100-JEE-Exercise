package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

    @Test
    public void testUpdateSubCategory(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        String name = "sub";
        SubCategoryDto subCategoryDto = new SubCategoryDto(null, name, categoryDto);
        subCategoryDto.id = createSubCategory(subCategoryDto);

        get("/id/" + subCategoryDto.id)
                .then()
                .body("name", is(name));

        //Put
        String newName = "newSub";
        subCategoryDto.name = newName;
        given().contentType(ContentType.JSON)
                .pathParam("id", subCategoryDto.id)
                .body(subCategoryDto)
                .put("/id/{id}")
                .then()
                .statusCode(204);

        get("/id/" + subCategoryDto.id)
                .then()
                .body("name", is(newName));
    }

    @Test
    public void testPatchSubCategoryName() {
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        String name = "sub";
        SubCategoryDto subCategoryDto = new SubCategoryDto(null, name, categoryDto);
        subCategoryDto.id = createSubCategory(subCategoryDto);

        get("/id/" + subCategoryDto.id)
                .then()
                .body("name", is(name));

        //Patch
        String newName = "newSub";
        given().contentType(ContentType.TEXT)
                .pathParam("id", subCategoryDto.id)
                .body(newName)
                .patch("/id/{id}")
                .then()
                .statusCode(204);

        get("/id/" + subCategoryDto.id)
                .then()
                .body("name", is(newName));
    }



    //TODO @Path("/parent/{id}")

    //TODO @Path("/id/{id}/subsubcategories")


    //TODO MORE THAN ONE
    //TODO test invalid requests.
}
