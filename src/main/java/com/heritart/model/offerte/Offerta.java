package com.heritart.model.offerte;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("offerte")
public class Offerta {

    @Id
    private String id;
    private String email;
    private String idOpera;
    private Integer valore;
    private Date data;

    public Offerta(String email, String idOpera, Integer valore){
        this.email = email;
        this.idOpera = idOpera;
        this.valore = valore;
        this.data = new Date();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getIdOpera() {
        return idOpera;
    }

    public Integer getValore() {
        return valore;
    }

    public Date getData() {
        return data;
    }

}
