package com.starwars.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanetaCompleto extends Planeta {

    public PlanetaCompleto() {
    }

    public PlanetaCompleto(String _id, String nome, String clima, String terreno, Integer qtdAparicoesEmFilmes) {
        super(_id, nome, clima, terreno);
        this.qtdAparicoesEmFilmes = qtdAparicoesEmFilmes;
    }

    @JsonProperty
    private Integer qtdAparicoesEmFilmes;

    @Override
    public Integer getQtdAparicoesEmFilmes() {
        return qtdAparicoesEmFilmes;
    }

    @Override
    public void setQtdAparicoesEmFilmes(Integer qtdAparicoesEmFilmes) {
        this.qtdAparicoesEmFilmes = qtdAparicoesEmFilmes;
    }
}
