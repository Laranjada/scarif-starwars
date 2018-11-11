package com.starwars.domain.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.starwars.application.binder.BindingObject;
import com.starwars.domain.persistente.Planeta;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BindingObject
public class CollectionPlanetaRepository {

    private static final Logger log = LoggerFactory.getLogger(CollectionPlanetaRepository.class);
    private static final Map<String, Planeta> persons = new HashMap<String, Planeta>();

    @Inject
    private MongoDatabase db;

    @Inject
    private MongoCollection<Document> collection;

    private void init(){
        if (persons.isEmpty()) {
            persons.put("FN1", new Planeta("FN1", "seco", "deserto"));
            persons.put("FN2", new Planeta("FN2", "umido", "agua"));
            persons.put("FN3", new Planeta("FN3", "seco", "montanhoso"));
            persons.put("FN4", new Planeta("FN4", "temperado", "variado"));
        }
    }

    public Planeta getByNome(String nome) {
        init();
        return persons.get(nome);
    }

    public List<Planeta> getAll() {
        init();
        List<Planeta> result = new ArrayList<Planeta>();
        result.addAll(persons.values());

        return result;
    }

    public int getCount() {
        init();
        return persons.size();
    }

    public void remove() {
        init();
        if (persons.keySet().isEmpty()) {
            log.info("Todos os planetas foram removidos");
            return;
        }

        persons.remove(persons.keySet().toArray()[0]);
    }

    public String save(Planeta planeta) {
        init();
        String result;
        if (persons.get(planeta.getNome()) == null) {
            result = "Added Planeta with nome= " + planeta.getNome();
        } else {
            result = "Updated Planeta with nome= " + planeta.getNome();
        }
        persons.put(planeta.getNome(), planeta);
        return result;
    }
}
