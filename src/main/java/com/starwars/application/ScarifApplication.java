package com.starwars.application;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.starwars.application.binder.StarWarsBinder;
import com.starwars.application.managed.MongoConnectionManaged;
import com.starwars.resource.PlanetaResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScarifApplication extends Application<ScarifConfig> {

    private static final Logger log = LoggerFactory.getLogger(ScarifApplication.class);

    private Injector serviceLocator;

    public static void main(String[] args) throws Exception {
        new ScarifApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ScarifConfig> bootstrap) {

        bootstrap.addBundle(new SwaggerBundle<ScarifConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ScarifConfig configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        super.initialize(bootstrap);
    }

    @Override
    public void run(ScarifConfig config, Environment env) {

        log.info("MongoDB connecting...");
        MongoConnectionManaged mongoManaged = new MongoConnectionManaged(config.repositoryConfig);
        env.lifecycle().manage(mongoManaged);

        log.info("Service locator init");
        serviceLocator = Guice.createInjector(new StarWarsBinder(mongoManaged.getDb(), mongoManaged.getCollection()));

        log.info("HealthCheck init");
        env.healthChecks().register("template",
                new ScarifHealthCheck(config.getVersion()));

        env.jersey().register(serviceLocator.getInstance(PlanetaResource.class));

    }
}
