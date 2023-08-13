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
import java.util.Date;

@Document("aste")
public class Asta {
    private @Id String id;
    private @Indexed(unique = true) String titolo;
    private String idGestore;
    private String descrizione;
    private Date dataInizio;
    private Date dataFine;
    private StatoAsta stato;
    private Binary photo;

    public Asta(String titolo, String idGestore) throws ParseException {
        this.titolo = titolo;
        this.idGestore = idGestore;
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

    public void setDataInizio(String dataInizio) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        this.dataInizio = sdf.parse(dataInizio);
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        this.dataFine = sdf.parse(dataFine);
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

    public Binary getPhoto() {
        return photo;
    }

    public void addPhoto(MultipartFile file) throws IOException {
        Binary photo = new Binary(BsonBinarySubType.BINARY, file.getBytes());
        this.photo = photo;
    }


}
