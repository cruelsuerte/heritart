package com.heritart.model.aste;

import com.heritart.model.opere.Tecnica;

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
