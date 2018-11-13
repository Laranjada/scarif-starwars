package com.starwars.application;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.client.MongoDatabase;

public class ScarifHealthCheck extends HealthCheck {

    private final String version;
    private MongoDatabase db;

    public ScarifHealthCheck(String version, MongoDatabase db) {
        this.version = version;
        this.db = db;
    }

        @Override
    protected Result check() throws Exception {

            try {
                long count = db.getCollection("planetas").countDocuments();
                return Result.healthy("StarWars Scarif version: " + this.version + " planet documents count=" + count);
            } catch (Exception e) {
                return Result.unhealthy("StarWars Scarif version: " + " mongodb out");
            }
        }
}
