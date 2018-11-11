package com.starwars.application.binder;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.util.Set;

public class StarWarsBinder extends AbstractModule{

    private MongoDatabase db;
    private MongoCollection<Document> collection;

    public StarWarsBinder(MongoDatabase db, MongoCollection<Document> collection) {
        super();
        this.db = db;
        this.collection= collection;
    }

    @Override
    protected void configure() {

        //binding classes
        getServiceClasses().stream().forEach(o-> bind(o));
        getSingletonServiceClasses().stream().forEach(o-> bind(o).in(Singleton.class));

    }

    @Provides
    MongoDatabase provideMongoDatabase() {
        return this.db;
    }

    @Provides
    MongoCollection<Document> provideMongoCollection() {
        return this.collection;
    }

    private Set<Class<?>> getSingletonServiceClasses() {
        Reflections reflections = new Reflections("com.starwars");
        Set<Class<?>> response  = reflections.getTypesAnnotatedWith(BindingSingletonResource.class);
        response.addAll(reflections.getTypesAnnotatedWith(BindingSingletonObject.class));
        return response;
    }

    private Set<Class<?>> getServiceClasses() {
        Reflections reflections = new Reflections("com.starwars");
        Set<Class<?>> response  = reflections.getTypesAnnotatedWith(BindingResource.class);
        response.addAll(reflections.getTypesAnnotatedWith(BindingObject.class));
        return response;
    }
}
