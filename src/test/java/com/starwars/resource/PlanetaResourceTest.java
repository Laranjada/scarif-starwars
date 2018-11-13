package com.starwars.resource;

import com.starwars.AbstractResourceTest;
import com.starwars.domain.PlanetaCompleto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlanetaResourceTest extends AbstractResourceTest{

    private Logger log = LoggerFactory.getLogger(PlanetaResourceTest.class);

    @Test
    public void testAHealthCheck() throws Exception {
        log.info("test health check");

        String response = when().get(new URL("http://localhost:9001/healthcheck"))
                .then().statusCode(200).extract().jsonPath().prettify();

        log.info("HealthCheck response= " + response);
    }

    @Test
    public void testBProcuraPorNomeNotFound () {
        log.info("test procura por nome not found");
        given().pathParam("nome", "Terra").when()
                .get("/procurar/{nome}").then().statusCode(404);
    }

    @Test
    public void testCProcuraPorIdNotFound () {
        log.info("test procura por id not found");
        given().pathParam("id", "5be93a783a789f56246c8c7f").when()
                .get("/{id}").then().statusCode(404);

        given().pathParam("id", "1").when()
                .get("/{id}").then().statusCode(400);
    }

    @Test
    public void testDInsertPlanetaScarif () {
        log.info("test insert planeta scarif");
        String scarifJSON = "{\"nome\":\"Scarif\",\"clima\":\"bom\",\"terreno\":\"TOP\"}";
        given().contentType(MIME_TYPE_JSON).body(scarifJSON).when().post().then().statusCode(201);

        PlanetaCompleto scarif = given().pathParam("nome", "Scarif").when()
                .get("/procurar/{nome}").then().statusCode(200).extract().as(PlanetaCompleto.class);

        assertNotNull(scarif);
        assertEquals(scarif.getNome(), "Scarif");
        assertEquals(scarif.getClima(), "bom");
        assertEquals(scarif.getTerreno(), "TOP");

        log.info("Scarif id: " + scarif.get_id());
    }

    @Test
    public void testEPesquisaPorNome () {
        log.info("test procura por nome");
        PlanetaCompleto scarif = given().pathParam("nome", "Scarif").when()
                .get("/procurar/{nome}").then().statusCode(200).extract().as(PlanetaCompleto.class);
        assertNotNull(scarif);
        assertEquals(scarif.getNome(), "Scarif");
    }


    @Test
    public void testFPesquisaPorId () {
        log.info("test procura por id");
        PlanetaCompleto scarif = given().pathParam("nome", "Scarif").when()
                .get("/procurar/{nome}").then().statusCode(200).extract().as(PlanetaCompleto.class);

        PlanetaCompleto scarifPorId = given().pathParam("id", scarif.get_id()).when()
                .get("/{id}").then().statusCode(200).extract().as(PlanetaCompleto.class);

        assertNotNull(scarifPorId);
        assertEquals(scarifPorId.getNome(), scarif.getNome());
        assertEquals(scarifPorId.get_id(), scarif.get_id());
        assertEquals(scarifPorId.getQtdAparicoesEmFilmes(), Integer.valueOf(0));
    }

    @Test
    public void testGAlterarScarif() {
        log.info("test update planeta scarif");
        String scarifJSON = "{\"nome\":\"Scarif\",\"clima\":\"nem tanto\",\"terreno\":\"pequeno\"}";
        given().contentType(MIME_TYPE_JSON).body(scarifJSON).when().put().then().statusCode(200);

        PlanetaCompleto scarif = given().pathParam("nome", "Scarif").when()
                .get("/procurar/{nome}").then().statusCode(200).extract().as(PlanetaCompleto.class);

        assertNotNull(scarif);
        assertEquals(scarif.getClima(), "nem tanto");
        assertEquals(scarif.getTerreno(), "pequeno");

    }

    @Test
    public void testHInsertPlanetaDagobah() {
        log.info("test insert planeta Dagobah");
        String scarifJSON = "{\"nome\":\"Dagobah\",\"clima\":\"all\",\"terreno\":\"TOP\"}";
        given().contentType(MIME_TYPE_JSON).body(scarifJSON).when().post().then().statusCode(201);

        PlanetaCompleto dagobah = given().pathParam("nome", "Dagobah").when()
                .get("/procurar/{nome}").then().statusCode(200).extract().as(PlanetaCompleto.class);

        assertNotNull(dagobah);
        assertTrue(dagobah.getQtdAparicoesEmFilmes() > 0);

        log.info("Dagobah id: " + dagobah.get_id());
    }

    @Test
    public void testIRemoveNaoEncontrado() {
        log.info("test remover planeta not found");
        given().pathParam("id", "5be93a783a789f56246c8c7f").when()
                .delete("/{id}").then().statusCode(404);

        given().pathParam("id", "1").when()
                .delete("/{id}").then().statusCode(400);
    }

    @Test
    public void testJRemoveDagobah() {
        log.info("test procura por id");
        PlanetaCompleto scarif = given().pathParam("nome", "Dagobah").when()
                .get("/procurar/{nome}").then().statusCode(200).extract().as(PlanetaCompleto.class);

        given().pathParam("id", scarif.get_id()).when()
                .delete("/{id}").then().statusCode(200);

        given().pathParam("nome", "Dagobah").when()
                .get("/procurar/{nome}").then().statusCode(404);
    }

}

