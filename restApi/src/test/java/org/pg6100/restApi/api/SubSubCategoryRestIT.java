package org.pg6100.restApi.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.restApi.dto.CategoryDto;
import org.pg6100.restApi.dto.SubCategoryDto;
import org.pg6100.restApi.dto.SubSubCategoryDto;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

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
                .statusCode(301);

        /*
        given().pathParam("id", subSubCategoryDto.id)
                .get("/id/{id}")
                .then()
                .body("name", is(newName));
                */
    }

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
                .statusCode(301);

        /*
        given().pathParam("id", subSubCategoryDto.id)
                .get("/id/{id}")
                .then()
                .body("subCategory.id", is(subCategoryDto2.id));
                */

    }

    //@Path("/parent/{id}")
    @Test
    public void testGetSubCategoriesOfParentCategory(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto1 = new SubCategoryDto(null, "sub1", categoryDto);
        subCategoryDto1.id = createSubCategory(subCategoryDto1);
        SubCategoryDto subCategoryDto2 = new SubCategoryDto(null, "sub2", categoryDto);
        subCategoryDto2.id = createSubCategory(subCategoryDto2);
        SubCategoryDto subCategoryDto3 = new SubCategoryDto(null, "sub3", categoryDto);
        subCategoryDto3.id = createSubCategory(subCategoryDto3);

        String subName = "thatSubSub";
        createSubSubCategory(new SubSubCategoryDto(null, "subsub1", subCategoryDto1));
        createSubSubCategory(new SubSubCategoryDto(null, subName, subCategoryDto3));
        createSubSubCategory(new SubSubCategoryDto(null, "subsub3", subCategoryDto1));

        get("/parent/" + subCategoryDto1.id)
                .then()
                .body("size()", is(2));
        get("/parent/" + subCategoryDto3.id)
                .then()
                .body("size()", is(1));
        get("/parent/" + subCategoryDto2.id)
                .then()
                .body("size()", is(0));

        List<SubSubCategoryDto> subSubCategories = Arrays.asList(given().accept(ContentType.JSON)
                .pathParam("id", subCategoryDto3.id)
                .get("/parent/{id}")
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDto[].class));
        assertTrue(subSubCategories.get(0).name.equals(subName));
    }

    //Invalid requests 4**
    //Now permanently redirects 3**
    @Test
    public void testPatchWithNonExistingSubSubCategory(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto.id = createSubCategory(subCategoryDto);

        SubSubCategoryDto subSubCategoryDto = new SubSubCategoryDto(null, "subsub", subCategoryDto);
        subSubCategoryDto.id = createSubSubCategory(subSubCategoryDto);

        given().contentType(ContentType.TEXT)
                .pathParam("id", subSubCategoryDto.id + "1")
                .body(subCategoryDto.id)
                .patch("/id/{id}")
                .then()
                .statusCode(301);
    }

    @Test
    public void testPatchWithNonExistingParent(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        SubCategoryDto subCategoryDto = new SubCategoryDto(null, "sub", categoryDto);
        subCategoryDto.id = createSubCategory(subCategoryDto);

        SubSubCategoryDto subSubCategoryDto = new SubSubCategoryDto(null, "subsub", subCategoryDto);
        subSubCategoryDto.id = createSubSubCategory(subSubCategoryDto);

        given().contentType(ContentType.TEXT)
                .pathParam("id", subSubCategoryDto.id)
                .body(subCategoryDto.id + "1")
                .patch("/id/{id}")
                .then()
                .statusCode(301);
    }

    @Test
    public void testDeleteNotSubSubCategory(){
        CategoryDto categoryDto = new CategoryDto(null, "cat");
        categoryDto.id = createCategory(categoryDto);

        given().pathParam("id", categoryDto.id)
                .delete("/id/{id}")
                .then().statusCode(301);
    }
}
