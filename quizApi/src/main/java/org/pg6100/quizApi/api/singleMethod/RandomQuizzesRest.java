package org.pg6100.quizApi.api.singleMethod;

import com.google.common.base.Throwables;
import io.swagger.annotations.*;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.quiz.ejb.QuestionEJB;
import org.pg6100.quizApi.dto.QuestionDto;
import org.pg6100.quizApi.api.Formats;
import org.pg6100.quizApi.dto.QuestionDto;
import org.pg6100.quizApi.dto.converter.QuestionConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Api(value = "/randomquizzes" , description = "Gets random quizzes")
@Path("/randomquizzes")
@Produces({
        Formats.V2_JSON,
        Formats.BASE_JSON
})

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RandomQuizzesRest {

    @EJB
    private CategoryEJB categoryEJB;

    @EJB
    private QuestionEJB questionEJB;

    @ApiOperation("Retrieve a set of questions bastion on your criteria")
    @POST
    public List<Long> getQuizzes(
            @ApiParam("Number of questions wanted(Default 5)")
            @DefaultValue("5")@QueryParam("n")
                    Integer numberOfQuestions,
            @ApiParam("Id of category/ sub or  sub sub if you want questions only from specific place.")
            @QueryParam("filter")
                    Long id){
        List<QuestionDto> questions = QuestionConverter.transform(questionEJB.getAllQuestions());

        if(questions == null || questions.size() < numberOfQuestions)
            throw new WebApplicationException("Not enough quizzes to fill: " + numberOfQuestions + " places.", 400);

        if(id == null) {
            return getIds(questions, numberOfQuestions, x -> true);
        } else if(categoryEJB.isCategoryPresent(id)) {
            return getIds(questions, numberOfQuestions, q -> q.subSubCategory.subCategory.category.id.equals(id.toString()));
        } else if(categoryEJB.isSubCategoryPresent(id)) {
            return getIds(questions, numberOfQuestions, q -> q.subSubCategory.subCategory.id.equals(id.toString()));
        } else if(categoryEJB.isSubSubCategoryPresent(id)) {
            return getIds(questions, numberOfQuestions, q -> q.subSubCategory.id.equals(id.toString()));
        } else {
            throw new WebApplicationException("No category, sub category or sub sub category with id " + id, 404);
        }
    }

    private List<Long> getIds(List<QuestionDto> questions, int numberOfQuestions, Predicate<QuestionDto> predicate){
        Random r = new Random();
        List<QuestionDto> filteredQuestions = questions.stream()
                .filter(predicate)
                .collect(Collectors.toList());

        if(filteredQuestions.size() < numberOfQuestions)
            throw new WebApplicationException("Not enough quizzes to fill: " + numberOfQuestions + " places.", 400);

        else if (filteredQuestions.size() == numberOfQuestions)
            return filteredQuestions.stream()
                    .map(q -> Long.valueOf(q.id))
                    .collect(Collectors.toList());

        else {
            Set<Long> ids = new HashSet<>();
            while (ids.size() != numberOfQuestions)
                ids.add(Long.valueOf(filteredQuestions.get(r.nextInt(filteredQuestions.size())).id));
            return new ArrayList<>(ids);
        }
    }

    private WebApplicationException wrapException(Exception e) throws WebApplicationException{
        Throwable cause = Throwables.getRootCause(e);
        if(cause instanceof ConstraintViolationException){
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
