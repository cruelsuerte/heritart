package com.heritart.model.aste;

public enum StatoAsta {
    PROGRAMMATA("In programma"),
    IN_CORSO("In corso"),
    TERMINATA("Terminata");

    private final String name;
    StatoAsta(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
