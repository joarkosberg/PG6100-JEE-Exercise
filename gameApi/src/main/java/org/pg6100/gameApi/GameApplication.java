package org.pg6100.gameApi;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.pg6100.gameApi.api.GameRestImpl;
import org.pg6100.gameApi.jdbi.GameDAO;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

public class GameApplication extends Application<GameConfiguration> {

    public static void main(String[] args) throws Exception {
        new GameApplication().run(args);
    }

    @Override
    public String getName() {
        return "Game API for quizzes delivered by restAPI";
    }

    @Override
    public void initialize(Bootstrap<GameConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", null, "a"));
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "b"));
        bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "c"));
        bootstrap.addBundle(new AssetsBundle("/assets/images", "/images", null, "d"));
        bootstrap.addBundle(new AssetsBundle("/assets/lang", "/lang", null, "e"));
        bootstrap.addBundle(new AssetsBundle("/assets/lib", "/lib", null, "f"));
    }

    @Override
    public void run(GameConfiguration configuration, Environment environment) {

        // DB config
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");

        // Init H2 testdata
        createAndAddData(jdbi);

        final GameDAO gameDAO = jdbi.onDemand(GameDAO.class);
        final GameRestImpl gameResource = new GameRestImpl(gameDAO);
        environment.jersey().setUrlPattern("/gameApi/api/*");
        environment.jersey().register(gameResource);

        //swagger
        environment.jersey().register(new ApiListingResource());

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/game/api");
        beanConfig.setResourcePackage("org.pg6100.gameApi.api");
        beanConfig.setScan(true);

        //add further configuration to activate SWAGGER
        environment.jersey().register(new io.swagger.jaxrs.listing.ApiListingResource());
        environment.jersey().register(new io.swagger.jaxrs.listing.SwaggerSerializers());
    }

    private void createAndAddData(DBI dbi) {
        try (Handle handle = dbi.open()) {
            handle.createCall("" +
                    "CREATE TABLE GAME" +
                    "(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "questions ARRAY," +
                    "answeredQuestions INT," +
                    ");").invoke();

            handle.createStatement("" +
                    "INSERT INTO GAME (questions, answeredQuestions)" +
                    "VALUES (?, ?)")
                    .bind(0, 5)
                    .bind(1, 2).execute();

            handle.createStatement("" +
                    "INSERT INTO GAME (questions, answeredQuestions)" +
                    "VALUES (?, ?)")
                    .bind(0, 3)
                    .bind(1, 0).execute();
        }
    }
}
