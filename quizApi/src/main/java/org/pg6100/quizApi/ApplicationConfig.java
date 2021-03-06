package org.pg6100.quizApi;

import io.swagger.jaxrs.config.BeanConfig;
import org.pg6100.quizApi.api.implementation.QuestionRestImpl;
import org.pg6100.quizApi.api.implementation.SubSubCategoryRestImpl;
import org.pg6100.quizApi.api.implementation.CategoryRestImpl;
import org.pg6100.quizApi.api.implementation.SubCategoryRestImpl;
import org.pg6100.quizApi.api.singleMethod.RandomQuizRest;
import org.pg6100.quizApi.api.singleMethod.RandomQuizzesRest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    private final Set<Class<?>> classes;

    public ApplicationConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/quiz/api");
        beanConfig.setResourcePackage("org.pg6100.quizApi");
        beanConfig.setScan(true);

        HashSet<Class<?>> c = new HashSet<>();
        c.add(CategoryRestImpl.class);
        c.add(SubCategoryRestImpl.class);
        c.add(SubSubCategoryRestImpl.class);
        c.add(QuestionRestImpl.class);
        c.add(RandomQuizRest.class);
        c.add(RandomQuizzesRest.class);

        //add further configuration to activate SWAGGER
        c.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        c.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        //needed to handle Java 8 dates
        c.add(ObjectMapperContextResolver.class);

        classes = Collections.unmodifiableSet(c);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}