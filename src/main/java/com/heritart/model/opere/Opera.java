package com.heritart.model.opere;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Document("opere")
public class Opera {
    @Id
    private String id;
    private String titolo;
    private String artista;
    private Integer annoCreazione;
    private String provenienza;
    private Tipologia tipologia;
    private String dimensioni;
    private String descrizione;
    private Proprieta proprieta;
    private CorrenteArtistica correnteArtistica;
    private Tecnica tecnica;
    private Materiale materiale;
    private Condizioni condizioni;
    private List<String> photoList= new ArrayList<>();
    private Integer offerta;
    private StatoOpera stato;
    private String idAsta;

    public Opera(String titolo, String artista, Integer annoCreazione, String provenienza,
                 Tipologia tipologia, Proprieta proprieta, Condizioni condizioni){
        this.titolo = titolo;
        this.artista = artista;
        this.annoCreazione = annoCreazione;
        this.provenienza = provenienza;
        this.tipologia = tipologia;
        this.proprieta = proprieta;
        this.condizioni = condizioni;
        this.offerta = 0;
        this.stato = StatoOpera.DISPONIBILE;
    }

    public String getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getArtista() {
        return artista;
    }

    public Integer getAnnoCreazione() {
        return annoCreazione;
    }

    public String getProvenienza() {
        return provenienza;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public String getDimensioni() {
        return dimensioni;
    }

    public void setDimensioni(String dimensioni) {
        this.dimensioni = dimensioni;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Proprieta getProprieta() {
        return proprieta;
    }

    public CorrenteArtistica getCorrenteArtistica() {
        return correnteArtistica;
    }

    public void setCorrenteArtistica(CorrenteArtistica correnteArtistica) {
        this.correnteArtistica = correnteArtistica;
    }

    public Tecnica getTecnica() {
        return tecnica;
    }

    public void setTecnica(Tecnica tecnica) {
        this.tecnica = tecnica;
    }

    public Materiale getMateriale() {
        return materiale;
    }

    public void setMateriale(Materiale materiale) {
        this.materiale = materiale;
    }

    public Condizioni getCondizioni() {
        return condizioni;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void addPhoto(MultipartFile file) throws IOException {
        Binary binPhoto = new Binary(BsonBinarySubType.BINARY, file.getBytes());
        String photo = Base64.getEncoder().encodeToString(binPhoto.getData());
        photoList.add(photo);
    }

    public Integer getOfferta() {
        return offerta;
    }

    public void setOfferta(Integer offerta) {
        this.offerta = offerta;
    }

    public StatoOpera getStato() {
        return stato;
    }

    public void setStato(StatoOpera stato) {
        this.stato = stato;
    }

    public String getIdAsta() {
        return idAsta;
    }

    public void setAsta(String idAsta) {
        if(stato == StatoOpera.DISPONIBILE){
            setStato(StatoOpera.ASTA);
            this.idAsta = idAsta;
        }
    }
}
