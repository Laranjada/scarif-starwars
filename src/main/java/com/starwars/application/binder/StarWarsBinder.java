package com.starwars.application.binder;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.util.Set;

public class StarWarsBinder extends AbstractBinder{

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
        getServiceClasses().stream().forEach(o-> bind(o).to(o));
        getSingletonServiceClasses().stream().forEach(o-> bind(o).to(o).in(Singleton.class));

        //binding MongoDatabase instance
        bind(this.db).to(MongoDatabase.class);

        //binding collection instance
        bind(this.collection);

    }

    MongoDatabase provideMongoDatabase() {
        return this.db;
    }

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
