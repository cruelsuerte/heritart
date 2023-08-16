package com.heritart.model.opere;

public enum Proprieta {

    COLLEZIONE_PRIVATA("Collezione privata"),
    COLLEZIONE_PUBBLICA("Collezione pubblica"),
    ARTISTA("Artista");

    private final String name;
    Proprieta(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
