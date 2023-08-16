package com.heritart.model.opere;

public enum Tecnica {
    OLIO("Olio su tela"),
    ACQUERELLO("Acquerello"),
    ACRILICO("Acrilico"),
    PASTELLO("Pastello"),
    INCHIOSTRO("Inchiostro"),
    MATITA("Matita"),
    COLLAGE("Collage"),
    SPRAY("Spray"),
    RASCHIATURA("Raschiatura"),
    MODELLATURA("Modellatura"),
    INTAGLIO("Intaglio"),
    ASSEMBLAGGIO("Assemblaggio"),
    TECNICA_MISTA("Tecnica mista"),
    DIGITALE("Digitale");
    private final String name;
    Tecnica(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
