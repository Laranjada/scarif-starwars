package com.starwars;

import com.starwars.restclient.SWAPIClient;
import com.starwars.restclient.json.PlanetSWAPI;
import com.starwars.util.SWAPIPlanetHelper;

import java.util.List;

public class Teste {

    public static void main(String[] args) {
 //       testSWAPIPlanetHelper();
//        testSWAPIClient();
    }

    private static void testSWAPIClient() {
        try {

            System.out.println("url " + "http://swapi.co/api/planets");
            SWAPIClient client = new SWAPIClient();
            SWAPIClient.SWAPIResponse response = client.listPlanets();

            System.out.println("response.isHttpExecutionStatus() = " + response.isHttpExecutionStatus());
            System.out.println("response.getPlanetListResponse().isPresent() = " + response.getPlanetListResponse().isPresent());
            response.getHttpResponse().ifPresent(r -> System.out.println("statusLine = " + r.getStatusLine()));
            response.getPlanetListResponse().ifPresent(pl -> System.out.println("pl.count = " + pl.getCount()));
            response.getPlanetListResponse().ifPresent(pl -> System.out.println("some planet: " + pl.getResults().stream().findFirst().get().toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testSWAPIPlanetHelper() {
        try {

            List<PlanetSWAPI> planetSWAPIS = SWAPIPlanetHelper.recuperaTodosOsPlanetas();

            System.out.println("planetSWAPIS.size() = " + planetSWAPIS.size());

            planetSWAPIS.stream().forEach(p -> System.out.println("p.name= " + p.getName() +
                    " n_aparicoes=" + SWAPIPlanetHelper.getNumeroAparicoesPlaneta(p)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
