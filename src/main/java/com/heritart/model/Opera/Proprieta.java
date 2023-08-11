package com.heritart.model.Opera;

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

    public static Proprieta get(String name){
        for (Proprieta p : values()) {
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

}
