package com.starwars.application;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RepositoryConfig {

    @JsonProperty
    private String user;

    @JsonProperty
    private String passwd;

    @JsonProperty
    private String host;

    @JsonProperty
    private Integer port;

    @JsonProperty
    private String db;

    @JsonProperty
    private String collectionName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public RepositoryConfig(String host, Integer port, String db, String collectionName, String user, String passwd) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.collectionName = collectionName;
        this.user = user;
        this.passwd = passwd;
    }

    public RepositoryConfig() {
    }
}
