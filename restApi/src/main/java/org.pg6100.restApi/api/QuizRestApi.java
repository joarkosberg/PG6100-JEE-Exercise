package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pg6100.restApi.dto.CategoryDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Api(value = "/category" , description = "Handling of creating and retrieving quiz data")
@Path("/category")
@Produces({
        Formats.BASE_JSON
})

public interface QuizRestApi {
    @ApiOperation("Retrieve a list of all the categories")
    @GET
    List<CategoryDto> getCategories();


}
