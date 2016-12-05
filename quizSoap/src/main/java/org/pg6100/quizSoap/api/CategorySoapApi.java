package org.pg6100.quizSoap.api;

import org.pg6100.quizSoap.dto.CategoryDto;

import javax.jws.WebService;
import java.util.List;

@WebService(name = "CategorySoap")
public interface CategorySoapApi {

    List<CategoryDto> getCategories(Boolean withQuizzes);

    Long createCategory(CategoryDto dto);

    CategoryDto getCategory(Long id);

    void updateCategory(Long id, CategoryDto dto);

    void patchCategoryName(Long id, String name);

    boolean deleteCategory(Long id);
}
