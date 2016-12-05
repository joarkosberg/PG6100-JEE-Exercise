package org.pg6100.quizSoap.api;

import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.quizSoap.dto.CategoryConverter;
import org.pg6100.quizSoap.dto.CategoryDto;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;
import java.util.List;

@WebService(
        endpointInterface = "org.pg6100.quizSoap.api.CategorySoapApi"
)
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CategorySoapImpl implements CategorySoapApi{

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public List<CategoryDto> getCategories(Boolean withQuizzes) {
        if(withQuizzes != null)
            if (withQuizzes)
                return CategoryConverter.transform(categoryEJB.getCategoriesWithQuestions());

        return CategoryConverter.transform(categoryEJB.getAllCategories());
    }

    @Override
    public Long createCategory(CategoryDto dto) {
        if(dto.id != null){
            return null;
        }
        Long id = categoryEJB.createNewCategory(dto.name);
        return id;
    }

    @Override
    public CategoryDto getCategory(Long id) {
        if (!categoryEJB.isCategoryPresent(id)) {
            return null;
        }
        return CategoryConverter.transform(categoryEJB.getCategory(id));
    }

    @Override
    public void updateCategory(Long id, CategoryDto dto) {
        categoryEJB.updateCategory(id, dto.name);
    }

    @Override
    public void patchCategoryName(Long id, String name) {
        categoryEJB.updateCategory(id, name);
    }


    public boolean deleteCategory(Long id) {
        if (!categoryEJB.isCategoryPresent(id)) {
            return false;
        }
        return categoryEJB.delete(id);
    }
}
