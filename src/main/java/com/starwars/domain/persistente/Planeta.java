package com.starwars.domain.persistente;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Planeta {

    private String nome;
    private String clima;
    private String terreno;

    @JsonProperty
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @JsonProperty
    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    @JsonProperty
    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    public Planeta() {
    }

    public Planeta(String nome, String clima, String terreno) {
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Planeta planeta = (Planeta) o;

        if (nome != null ? !nome.equals(planeta.nome) : planeta.nome != null) return false;
        if (clima != null ? !clima.equals(planeta.clima) : planeta.clima != null) return false;
        return terreno != null ? terreno.equals(planeta.terreno) : planeta.terreno == null;
    }

    @Override
    public int hashCode() {
        int result = nome != null ? nome.hashCode() : 0;
        result = 31 * result + (clima != null ? clima.hashCode() : 0);
        result = 31 * result + (terreno != null ? terreno.hashCode() : 0);
        return result;
    }
}