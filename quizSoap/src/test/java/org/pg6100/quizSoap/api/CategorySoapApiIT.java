package org.pg6100.quizSoap.api;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pg6100.quizSoap.client.*;

import javax.xml.ws.BindingProvider;
import java.util.List;
import static org.junit.Assert.*;

public class CategorySoapApiIT {

    private static CategorySoap ws;

    @BeforeClass
    public static void initClass() {
        CategorySoapImplService service = new CategorySoapImplService();
        ws = service.getCategorySoapImplPort();

        String url = "http://localhost:8080/quizsoap/CategorySoapImpl";

        ((BindingProvider)ws).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    }

    @Before
    @After
    public void cleanData(){
        List<CategoryDto> list = ws.getCategories(false);
        list.forEach(dto -> ws.deleteCategory(dto.getId()));
    }

    @Test
    public void testCreateAndGet() {
        assertEquals(0, ws.getCategories(false).size());

        String name = "MittNavn";
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);

        Long id = ws.createCategory(categoryDto);
        assertEquals(1, ws.getCategories(false).size());

        CategoryDto createdCategory = ws.getCategory(id);

        assertEquals(id, createdCategory.getId());
        assertEquals(name, createdCategory.getName());
    }

    @Test
    public void testCreateInvalidCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("RNAOPS");
        categoryDto.setId(Long.valueOf(1234)); //Is generated, should not make own.

        ws.createCategory(categoryDto);
        assertEquals(0, ws.getCategories(false).size());
    }

    @Test
    public void testUpdateCategory(){
        String name = "MyName";
        String newName = "MyNewName";
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);

        Long id = ws.createCategory(categoryDto);
        CategoryDto createdCategory = ws.getCategory(id);

        assertEquals(name, createdCategory.getName());
        assertNotEquals(newName, createdCategory.getName());

        createdCategory.setName(newName);
        ws.updateCategory(id, createdCategory);

        CategoryDto updatedCategory = ws.getCategory(id);

        assertNotEquals(name, updatedCategory.getName());
        assertEquals(newName, updatedCategory.getName());
    }
}
