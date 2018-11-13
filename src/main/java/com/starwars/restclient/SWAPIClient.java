package com.starwars.restclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.starwars.restclient.json.PlanetListSWAPI;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class SWAPIClient {

    private static final Logger log = LoggerFactory.getLogger(SWAPIClient.class);

    private final ObjectMapper mapper;

    private static final String URL_PLANETS = "http://swapi.co/api/planets";

    public SWAPIClient() {
        this.mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public SWAPIResponse listPlanets() throws Exception{
        return listPlanets(URL_PLANETS);
    }

    public SWAPIResponse listPlanets(String pageUrl) throws Exception{

        log.debug("SWAPI client send list planets request");

        HttpResponse response = null;
        boolean httpExecutionStatus = false;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet postRequest = new HttpGet(pageUrl);

            log.debug("request ready, GO!");
            response = client.execute(postRequest);

            log.debug("List planets response: [ statusCode: " + response.getStatusLine().getStatusCode() +
                    ", body length: "+ response.getEntity().getContentLength() +
                    ", message: " + response.getStatusLine().getReasonPhrase()
                    + "]");

        } catch (Exception e) {
            log.error("Listing fail: "+ e.getMessage(), e);

        } finally {
            httpExecutionStatus = response != null
                    && ( response.getStatusLine().getStatusCode() >= 200 ||
                     response.getStatusLine().getStatusCode() < 300 );
        }

        log.debug("SWAPI client parse response");
        return new SWAPIResponse(response, parseJson(response), httpExecutionStatus);

    }

    private PlanetListSWAPI parseJson(HttpResponse response) throws Exception{

        if(response == null || response.getStatusLine().getStatusCode() < 200
                || response.getStatusLine().getStatusCode() >= 300) return null;

        log.debug("http status code is " + response.getStatusLine().getStatusCode());
        String json = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.debug("RETURNED JSON: [" + json + "]");

        return mapper.readValue(json, PlanetListSWAPI.class);
    }

    public class SWAPIResponse {

        private final HttpResponse response;
        private final boolean httpExecutionStatus;
        private final PlanetListSWAPI planetListResponse;

        public SWAPIResponse(HttpResponse response, PlanetListSWAPI jsonBody, boolean httpExecutionStatus) {
            this.response = response;
            this.httpExecutionStatus = httpExecutionStatus;
            this.planetListResponse = jsonBody;
        }

        public Optional<HttpResponse> getHttpResponse() {
            return Optional.ofNullable(response);
        }

        public boolean isHttpExecutionStatus() {
            return httpExecutionStatus;
        }

        public Optional<PlanetListSWAPI> getPlanetListResponse() {
            return Optional.ofNullable(planetListResponse);
        }

    }

}
