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
                .statusCode(301);

        /*
        get("/id/" + subCategoryDto.id)
                .then()
                .body("name", is(newName));
                */
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
                .statusCode(301);

        /*
        get("/id/" + subCategoryDto.id)
                .then()
                .body("name", is(newName));
                */
    }

    //@Path("/parent/{id}")
    @Test
    public void testGetSubCategoriesOfParentCategory(){
        CategoryDto categoryDto1 = new CategoryDto(null, "cat");
        categoryDto1.id = createCategory(categoryDto1);
        CategoryDto categoryDto2 = new CategoryDto(null, "cat");
        categoryDto2.id = createCategory(categoryDto2);

        createSubCategory(new SubCategoryDto(null, "sub", categoryDto2));
        createSubCategory(new SubCategoryDto(null, "sub", categoryDto1));
        createSubCategory(new SubCategoryDto(null, "sub", categoryDto2));

        get("/parent/" + categoryDto1.id)
                .then()
                .body("size()", is(1));
        get("/parent/" + categoryDto2.id)
                .then()
                .body("size()", is(2));
    }


    //@Path("/id/{id}/subsubcategories")
    @Test
    public void testGetSubSubCategoriesOfSubCategory(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto1 = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto1.id = createSubCategory(subCategoryDto1);
        SubCategoryDto subCategoryDto2 = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto2.id = createSubCategory(subCategoryDto2);
        SubCategoryDto subCategoryDto3 = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto3.id = createSubCategory(subCategoryDto3);

        createSubSubCategory(new SubSubCategoryDto(null, "subsub", subCategoryDto3));
        createSubSubCategory(new SubSubCategoryDto(null, "subsub", subCategoryDto2));
        createSubSubCategory(new SubSubCategoryDto(null, "subsub", subCategoryDto3));

        given().pathParam("id", subCategoryDto1.id)
                .get("/id/{id}/subsubcategories")
                .then()
                .body("size()", is(0));
        given().pathParam("id", subCategoryDto2.id)
                .get("/id/{id}/subsubcategories")
                .then()
                .body("size()", is(1));
        given().pathParam("id", subCategoryDto3.id)
                .get("/id/{id}/subsubcategories")
                .then()
                .body("size()", is(2));
    }


    //Invalid requests 4**
    //Now deprecated 3**
    @Test
    public void testUpdateWithIdNotLong(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto1 = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto1.id = createSubCategory(subCategoryDto1);

        given().contentType(ContentType.JSON)
                .pathParam("id", subCategoryDto1.id)
                .body(new SubCategoryDto("hiNotANumber", "hei", new CategoryDto()))
                .put("/id/{id}")
                .then()
                .statusCode(301);
    }

    @Test
    public void testUpdateId(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto1 = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto1.id = createSubCategory(subCategoryDto1);

        given().contentType(ContentType.JSON)
                .pathParam("id", subCategoryDto1.id)
                .body(new SubCategoryDto(subCategoryDto1.id + "2", "hei", new CategoryDto()))
                .put("/id/{id}")
                .then()
                .statusCode(301);
    }

    @Test
    public void testUpdateOfNonExistingSubCategory(){
        given().contentType(ContentType.JSON)
                .pathParam("id", "kappa")
                .body(new SubCategoryDto())
                .put("/id/{id}")
                .then()
                .statusCode(404);
    }
}
