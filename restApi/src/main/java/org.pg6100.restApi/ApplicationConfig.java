package org.pg6100.restApi;

import io.swagger.jaxrs.config.BeanConfig;
import org.pg6100.restApi.api.CategoryRestImpl;

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
        beanConfig.setVersion("1.0.0"); // note the change in version from 0.0.1
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/quiz/api");
        beanConfig.setResourcePackage("org.pg6100.restApi");
        beanConfig.setScan(true);

        HashSet<Class<?>> c = new HashSet<>();
        c.add(CategoryRestImpl.class);

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