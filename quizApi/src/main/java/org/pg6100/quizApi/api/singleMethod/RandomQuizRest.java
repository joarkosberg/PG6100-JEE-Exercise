package org.pg6100.quizApi.api.singleMethod;

import io.swagger.annotations.*;
import org.pg6100.quiz.ejb.CategoryEJB;
import org.pg6100.quiz.ejb.QuestionEJB;
import org.pg6100.quizApi.dto.QuestionDto;
import org.pg6100.quizApi.api.Formats;
import org.pg6100.quizApi.dto.converter.QuestionConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "/randomquiz" , description = "Gets random quiz")
@Path("/randomquiz")
@Produces({
        Formats.V1_JSON,
        Formats.BASE_JSON
})

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RandomQuizRest {

    @EJB
    private CategoryEJB categoryEJB;

    @EJB
    private QuestionEJB questionEJB;

    @ApiOperation("Retrieve a random question from a category, sub category, sub sub category or random")
    @ApiResponses({
            @ApiResponse(code = 307, message = "Temporary redirect to your quiz!"),
            @ApiResponse(code = 400, message = "No questions created"),
            @ApiResponse(code = 404, message = "No category, sub category or sub sub category with that id"),
            @ApiResponse(code = 409, message = "No questions created for that id")
    })
    @GET
    public Response get(
            @ApiParam("ID of category, sub category or sub sub category to fetch a quiz from (Leave blank if you want total random")
            @QueryParam("filter")
                    Long id){
        Random r = new Random();
        List<QuestionDto> questions = QuestionConverter.transform(questionEJB.getAllQuestions());
        List<QuestionDto> filteredQuestions;
        String quizId;

        if(questions == null || questions.size() < 1)
            return Response.status(400).build();

        if(id == null) {
            quizId = questions.get(r.nextInt(questions.size())).id;
        } else if(categoryEJB.isCategoryPresent(id)) {
            filteredQuestions = questions
                    .stream()
                    .filter(q -> q.subSubCategory.subCategory.category.id.equals(id.toString()))
                    .collect(Collectors.toList());
            if(filteredQuestions.size() < 1) return Response.status(409).build();
            quizId = filteredQuestions.get(r.nextInt(filteredQuestions.size())).id;
        } else if(categoryEJB.isSubCategoryPresent(id)) {
            filteredQuestions = questions
                    .stream()
                    .filter(q -> q.subSubCategory.subCategory.id.equals(id.toString()))
                    .collect(Collectors.toList());
            if(filteredQuestions.size() < 1) return Response.status(409).build();
            quizId = filteredQuestions.get(r.nextInt(filteredQuestions.size())).id;
        } else if(categoryEJB.isSubSubCategoryPresent(id)) {
            filteredQuestions = questions
                    .stream()
                    .filter(q -> q.subSubCategory.id.equals(id.toString()))
                    .collect(Collectors.toList());
            if(filteredQuestions.size() < 1) return Response.status(409).build();
            quizId = filteredQuestions.get(r.nextInt(filteredQuestions.size())).id;
        } else {
            return Response.status(404).build();
        }

        return Response.status(307)
                .location(UriBuilder.fromUri("quizzes/" + quizId).build())
                .build();
    }
}
