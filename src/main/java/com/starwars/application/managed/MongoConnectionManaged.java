package com.starwars.application.managed;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.starwars.application.RepositoryConfig;
import io.dropwizard.lifecycle.Managed;
import org.bson.Document;

public class MongoConnectionManaged implements Managed {
    private Mongo mongo;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    private static final String ADMIN_DB_NAME = "admin";

    public MongoConnectionManaged(RepositoryConfig mongoConfig) {

        ServerAddress address = new ServerAddress(mongoConfig.getHost(), mongoConfig.getPort());

        MongoCredential credential = MongoCredential
                .createCredential(mongoConfig.getUser(), ADMIN_DB_NAME, mongoConfig.getPasswd().toCharArray());

        MongoClient mongoClient = new MongoClient(address, credential, MongoClientOptions.builder().build());

        this.mongo = mongoClient;
        this.db = mongoClient.getDatabase(mongoConfig.getDb());
        this.collection = db.getCollection(mongoConfig.getCollectionName());
    }

    public MongoDatabase getDb() {
        return db;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        mongo.close();
    }
}


