package org.pg6100.gameApi;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

import org.pg6100.gameApi.api.GameRestImpl;
import org.pg6100.gameApi.jdbi.GameDAO;

import org.skife.jdbi.v2.DBI;

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
    public void run(GameConfiguration configuration, Environment environment){

        //DB config
        final DBIFactory factory = new DBIFactory();
        final DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        final DBI jdbi = factory.build(environment, dataSourceFactory, "h2");

        final GameDAO gameDAO = jdbi.onDemand(GameDAO.class);
        final GameRestImpl gameResource = new GameRestImpl(gameDAO);

        //Init H2 db and test data
        gameDAO.createGameTable();
        addExampleData(gameDAO);

        environment.jersey().setUrlPattern("/game/api/*");
        environment.jersey().register(gameResource);

        //Swagger
        environment.jersey().register(new ApiListingResource());

        //Swagger at: localhost:8081/index.html
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8081");
        beanConfig.setBasePath("/game/api");
        beanConfig.setResourcePackage("org.pg6100.gameApi.api");
        beanConfig.setScan(true);

        //Add further configuration to activate SWAGGER
        environment.jersey().register(new io.swagger.jaxrs.listing.ApiListingResource());
        environment.jersey().register(new io.swagger.jaxrs.listing.SwaggerSerializers());
    }

    private void addExampleData(GameDAO gameDAO) {
        gameDAO.insert(new Long[]{Long.valueOf(12), Long.valueOf(2), Long.valueOf(41),
                Long.valueOf(132), Long.valueOf(122)}, 3);
        gameDAO.insert(new Long[]{Long.valueOf(312), Long.valueOf(2), Long.valueOf(61),
                Long.valueOf(132), Long.valueOf(132)}, 1);
        gameDAO.insert(new Long[]{Long.valueOf(12), Long.valueOf(62), Long.valueOf(12),
                Long.valueOf(132), Long.valueOf(12), Long.valueOf(192), Long.valueOf(1)}, 2);
    }
}
