package com.heritart.model.opere;

public enum CorrenteArtistica {
    CLASSICISMO("Classicismo"),
    RINASCIMENTO("Rinascimento"),
    BAROCCO("Barocco"),
    IMPRESSIONISMO("Impressionismo"),
    ESPRESSIONISMO("Espressionismo"),
    CUBISMO("Cubismo"),
    SURREALISMO("Surrealismo"),
    REALISMO("Realismo"),
    FUTURISMO("Futurismo"),
    DADAISMO("Dadaismo"),
    ARTE_ASTRATTA("Arte astratta"),
    ARTE_CINETICA("Arte cinetica"),
    POP_ART("Pop Art"),
    MINIMALISMO("Minimalismo"),
    ARTE_CONTEMPORANEA("Arte contemporanea"),
    ARTE_CONCETTUALE("Arte concettuale"),
    ARTE_DIGITALE("Arte digitale"),
    STREET_ART("Street Art"),
    FOTOREALISMO("Fotorealismo");
    private final String name;
    CorrenteArtistica(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
