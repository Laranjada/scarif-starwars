package com.starwars.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Planeta {

    protected String _id;
    protected String nome;
    protected String clima;
    protected String terreno;
    protected Integer qtdAparicoesEmFilmes;

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

    @JsonProperty
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @JsonProperty
    public Integer getQtdAparicoesEmFilmes() {
        return qtdAparicoesEmFilmes;
    }

    public void setQtdAparicoesEmFilmes(Integer qtdAparicoesEmFilmes) {
        this.qtdAparicoesEmFilmes = qtdAparicoesEmFilmes;
    }

    public Planeta() {
    }

    public Planeta(String _id, String nome, String clima, String terreno) {
        this._id = _id;
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