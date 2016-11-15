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

    @Test
    public void testUpdateSubSubCategory(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto.id = createSubCategory(subCategoryDto);

        String name = "subsub";
        SubSubCategoryDto subSubCategoryDto = new SubSubCategoryDto(null, name, subCategoryDto);
        subSubCategoryDto.id = createSubSubCategory(subSubCategoryDto);

        given().pathParam("id", subSubCategoryDto.id)
                .get("/id/{id}")
                .then()
                .body("name", is(name));

        //Put
        String newName = "newSubSub";
        subSubCategoryDto.name = newName;
        given().contentType(ContentType.JSON)
                .pathParam("id", subSubCategoryDto.id)
                .body(subSubCategoryDto)
                .put("/id/{id}")
                .then()
                .statusCode(204);

        given().pathParam("id", subSubCategoryDto.id)
                .get("/id/{id}")
                .then()
                .body("name", is(newName));
    }

    //TODO PATCH
    @Test
    public void testPatchSubSubCategoryParent(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto1 = new SubCategoryDto(null, "sub1", categoryDto);
        subCategoryDto1.id = createSubCategory(subCategoryDto1);
        SubCategoryDto subCategoryDto2 = new SubCategoryDto(null, "sub2", categoryDto);
        subCategoryDto2.id = createSubCategory(subCategoryDto2);

        SubSubCategoryDto subSubCategoryDto = new SubSubCategoryDto(null, "subsub", subCategoryDto1);
        subSubCategoryDto.id = createSubSubCategory(subSubCategoryDto);

        given().pathParam("id", subSubCategoryDto.id)
                .get("/id/{id}")
                .then()
                .body("subCategory.id", is(subCategoryDto1.id));

       //Patch parent sub category
        given().contentType(ContentType.TEXT)
                .pathParam("id", subSubCategoryDto.id)
                .body(subCategoryDto2.id)
                .patch("/id/{id}")
                .then()
                .statusCode(204);

        given().pathParam("id", subSubCategoryDto.id)
                .get("/id/{id}")
                .then()
                .body("subCategory.id", is(subCategoryDto2.id));

    }

    //TODO @Path("/parent/{id}")


    //TODO test invalid requests.
    //TODO MORE THAN ONE


}
