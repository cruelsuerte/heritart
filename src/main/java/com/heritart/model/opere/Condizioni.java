package com.heritart.model.opere;

public enum Condizioni {
    NUOVA("Nuova"),
    OTTIME("In ottime condizioni"),
    BUONE("In buone condizioni"),
    NECESSITA_RESTAURO("Necessita di restauro"),
    PROBLEMI_AUTENTICITA("Problemi di autenticità");
    private final String name;
    Condizioni(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public static Condizioni get(String name){
        for (Condizioni c : values()) {
            if (c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }
}
