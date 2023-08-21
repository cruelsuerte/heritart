package com.heritart.model.aste;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Document("aste")
public class Asta {
    private @Id String id;
    private String titolo;
    private String idGestore;
    private String descrizione;
    private Date dataInizio;
    private Date dataFine;
    private StatoAsta stato;
    private String photo;

    public Asta(String titolo, String idGestore, Date dataInizio, Date dataFine) throws ParseException {
        this.titolo = titolo;
        this.idGestore = idGestore;

        this.dataInizio = dataInizio;
        this.dataFine = dataFine;

        this.stato = StatoAsta.PROGRAMMATA;
    }


    public String getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getGestore() {
        return idGestore;
    }

    public Date getDataInizio() {
        return dataInizio;
    }
    public Date getDataFine() {
        return dataFine;
    }
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public StatoAsta getStato() {
        return stato;
    }

    public void setStato(StatoAsta stato) {
        this.stato = stato;
    }

    public String getPhoto() {
        return photo;
    }

    public void addPhoto(MultipartFile file) throws IOException {
        Binary binPhoto = new Binary(BsonBinarySubType.BINARY, file.getBytes());
        String photo = Base64.getEncoder().encodeToString(binPhoto.getData());
        this.photo = photo;
    }

    public boolean isInCorso(){
        Date now = new Date();
        return (stato == StatoAsta.PROGRAMMATA && now.after(dataInizio));
    }

    public boolean isTerminata(){
        Date now = new Date();
        return (stato == StatoAsta.IN_CORSO && now.after(dataFine));
    }


}
