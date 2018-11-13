package com.starwars;

import com.jayway.restassured.RestAssured;
import com.starwars.application.ScarifApplication;
import com.starwars.application.ScarifConfig;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;

public class AbstractResourceTest {

    public static final String MIME_TYPE_JSON = "application/json";

    public static final String MONGO_USER = "user";
    public static final String MONGO_PASSWD = "passwd";

    @ClassRule
    public static DropwizardAppRule<ScarifConfig> applicationRule;
    public static GenericContainer mongo;
    static {
        mongo = new GenericContainer("mongo:latest")
                .withExposedPorts(27017)
                .withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_USER)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_PASSWD);
        mongo.start();

        applicationRule =
                new DropwizardAppRule<>(
                        ScarifApplication.class,
                        "config.yml",
                        ConfigOverride.config("repository.host", mongo.getContainerIpAddress()),
                        ConfigOverride.config("repository.port", String.valueOf(mongo.getMappedPort(27017))),
                        ConfigOverride.config("repository.user", MONGO_USER),
                        ConfigOverride.config("repository.passwd", MONGO_PASSWD),
                        ConfigOverride.config("repository.db", "starwars"),
                        ConfigOverride.config("repository.collectionName", "planetas")
                );
    }

    @BeforeClass
    public static void startDropwizardServer() throws Exception {
        RestAssured.port = 9000;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/planeta";
    }
}
