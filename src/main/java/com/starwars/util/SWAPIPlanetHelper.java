package com.starwars.util;

import com.starwars.restclient.SWAPIClient;
import com.starwars.restclient.json.PlanetListSWAPI;
import com.starwars.restclient.json.PlanetSWAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class SWAPIPlanetHelper {

    public static List<PlanetSWAPI > recuperaTodosOsPlanetas() {

        Logger log = LoggerFactory.getLogger(SWAPIPlanetHelper.class);

        try {
            SWAPIClient swapiClient = new SWAPIClient();
            return pages(null, swapiClient);

        } catch (Exception e) {
            log.error("Falha ao recuperar os planetas.");
            log.error("Erro SWAPI: " + e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }

    private static List<PlanetSWAPI> pages(String pageUrl, SWAPIClient swapiClient ) throws Exception{

        List<PlanetSWAPI> result = Collections.EMPTY_LIST;

        SWAPIClient.SWAPIResponse response = pageUrl == null ?
                swapiClient.listPlanets() :
                swapiClient.listPlanets(pageUrl);

        if (!response.isHttpExecutionStatus() ||
                !response.getPlanetListResponse().isPresent()) {
            return result;
        }

        if (response.getPlanetListResponse().isPresent()) {
            PlanetListSWAPI respList = response.getPlanetListResponse().get();
            result = respList.getResults();

            if (respList.getNext() != null) {
                result.addAll(pages(respList.getNext(), swapiClient));
            }

        }

        return result;
    }

    public static int getNumeroAparicoesPlaneta(PlanetSWAPI planeta) {
        return planeta.getFilms() == null? 0 :
                planeta.getFilms().size();
    }
}
