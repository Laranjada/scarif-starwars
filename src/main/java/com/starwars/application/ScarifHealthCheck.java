package com.starwars.application;

import com.starwars.application.binder.BindingObject;
import com.starwars.domain.repository.CollectionPlanetaRepository;
import com.codahale.metrics.health.HealthCheck;

import javax.inject.Inject;

public class ScarifHealthCheck extends HealthCheck {
    private final String version;

    public ScarifHealthCheck(String version) {
        this.version = version;
    }

        @Override
    protected Result check() throws Exception {

        return Result.healthy("OK with version: " + this.version);
    }
}
