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
        List<CategoryDto> list = ws.getCategories(null);
        list.forEach(dto -> ws.deleteCategory(dto.getId()));
    }

    @Test
    public void testCreateAndGet() {
        assertEquals(0, ws.getCategories(null).size());

        String name = "MittNavn";
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);

        Long id = ws.createCategory(categoryDto);
        assertEquals(1, ws.getCategories(null).size());

        CategoryDto createdCategory = ws.getCategory(id);

        assertEquals(id,      createdCategory.getId());
        assertEquals(name,  createdCategory.getName());
    }
}
