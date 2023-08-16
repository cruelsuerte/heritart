package com.heritart.model.utenti;

public enum Ruolo {
    CLIENTE("Cliente"),
    GESTORE("Gestore");
    private final String name;
    Ruolo(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}