package com.starwars.restclient.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PlanetListSWAPI {

    @JsonProperty
    private Integer count;

    @JsonProperty
    private String previous;

    @JsonProperty
    private String next;

    @JsonProperty
    private List<PlanetSWAPI> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<PlanetSWAPI> getResults() {
        return results;
    }

    public void setResults(List<PlanetSWAPI> results) {
        this.results = results;
    }
}
