package com.starwars.domain.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.starwars.application.binder.BindingObject;
import com.starwars.domain.Planeta;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@BindingObject
public class PlanetaRepository {

    private static final Logger log = LoggerFactory.getLogger(PlanetaRepository.class);

    private static final String PLANETAS = "planetas";

    @Inject
    private MongoDatabase db;

    private final ObjectMapper mapper;

    public PlanetaRepository() {
        this.mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Optional<Planeta> getByNome(String nome) {
        return db.getCollection(PLANETAS)
                .find(eq("nome", nome)).into(new ArrayList<>()).stream()
                .map(PlanetaRepository::convert).findFirst();
    }

    public Optional<Planeta> getById(String _id) {
        return db.getCollection(PLANETAS)
                .find(eq("_id", new ObjectId(_id))).into(new ArrayList<>()).stream()
                .map(PlanetaRepository::convert).findFirst();
    }

    public List<Planeta> getAll() {
        return db.getCollection(PLANETAS).find().into(new ArrayList<>()).stream()
                .map(PlanetaRepository::convert).collect(Collectors.toList());
    }

    public long getCount() {
        return db.getCollection(PLANETAS).countDocuments();
    }

    public boolean remove(String _id) {
        DeleteResult result = db.getCollection(PLANETAS).deleteOne(eq("_id", new ObjectId(_id)));
        return result.getDeletedCount() > 0;
    }

    public boolean save(Planeta planeta) {
        try {
            db.getCollection(PLANETAS).insertOne(parse(planeta));
            return true;
        } catch (Exception e) {
            log.error("Falha ao salvar objeto: " + e.getMessage());
            return false;

        }
    }

    public boolean update(Planeta planeta) {
        try {
            Bson filter = planeta.get_id() == null ?
                new Document("nome", planeta.getNome()) :
                new Document("_id", new ObjectId(planeta.get_id()));

            planeta.set_id(null);
            Bson updateOperationDocument = new Document("$set", parse(planeta));

            return 0 < db.getCollection(PLANETAS).updateOne(filter, updateOperationDocument).getModifiedCount();
        } catch (Exception e) {
            log.error("Falha ao salvar objeto: " + e.getMessage());
            return false;

        }
    }

    private Document parse(Planeta planeta) throws Exception{
        return new Document(BasicDBObject.parse(mapper.writeValueAsString(planeta)));
    }

    private static Planeta convert(Document planeta){

        Planeta result = new Planeta();
        result.set_id(planeta.getObjectId("_id").toString());
        result.setNome(planeta.getString("nome"));
        result.setClima(planeta.getString("clima"));
        result.setTerreno(planeta.getString("terreno"));
        return result;
    }
}
