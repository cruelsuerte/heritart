package com.heritart.control;

import com.heritart.services.AuthenticationService;
import com.heritart.dao.AsteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.opere.*;
import com.heritart.model.utenti.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.constraints.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Controller
@Validated
public class ControllerGestore {

    @Autowired
    OpereRepository opereRepository;

    @Autowired
    AsteRepository asteRepository;

    @Autowired
    AuthenticationService authenticationService;


    @GetMapping("/Gestore/newOpera")
    public String opera(Model model){

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();

        model.addAttribute("name", name);
        model.addAttribute("tipologiaList", Tipologia.values());
        model.addAttribute("condizioniList", Condizioni.values());
        model.addAttribute("tecnicaList", Tecnica.values());
        model.addAttribute("correnteArtisticaList", CorrenteArtistica.values());
        model.addAttribute("materialeList", Materiale.values());
        model.addAttribute("proprietaList", Proprieta.values());
        return "aggiungi_opera";
    }

    @PostMapping("/Gestore/newOpera")
    public String newOpera(@RequestParam @NotBlank String titolo,
                           @RequestParam @NotBlank String artista,
                           @RequestParam @Min(0) @Max(2023) Integer anno,
                           @RequestParam @NotBlank String provenienza,
                           @RequestParam Tipologia tipologia,
                           @RequestParam Proprieta proprieta,
                           @RequestParam Condizioni condizioni,
                           @RequestParam(defaultValue = "0") @Min(0) int dim1,
                           @RequestParam(defaultValue = "0") @Min(0) int dim2,
                           @RequestParam(defaultValue = "0") @Min(0) int dim3,
                           @RequestParam(required = false) String descrizione,
                           @RequestParam(required = false) CorrenteArtistica correnteArtistica,
                           @RequestParam(required = false) Tecnica tecnica,
                           @RequestParam(required = false) Materiale materiale,
                           @RequestParam(defaultValue = "0") @Min(0) Integer baseAsta,
                           @RequestParam(required = false) @Size(max = 6) MultipartFile[] files,
                           RedirectAttributes redirectAttributes) throws IOException {


        Opera opera = new Opera(titolo, artista, anno, provenienza, tipologia, proprieta, condizioni);

        if (dim1 > 0 && dim2 > 0){
            String dimensioni = dim1 + " x " + dim2;
            if (dim3 > 0){
                dimensioni = dimensioni + " x " + dim3;
            }
            dimensioni = dimensioni + " (cm)";
            opera.setDimensioni(dimensioni);
        }

        if(descrizione != null && !descrizione.isBlank()){
            opera.setDescrizione(descrizione);
        }

        if(correnteArtistica != null){
            opera.setCorrenteArtistica(correnteArtistica);
        }

        if(tecnica != null){
            opera.setTecnica(tecnica);
        }

        if(materiale != null){
            opera.setMateriale(materiale);
        }

        if(baseAsta > 0){
            opera.setOfferta(baseAsta);
        }

        if (files != null) {
            for (MultipartFile file : files) {
                opera.addPhoto(file);
            }
        }

        opereRepository.save(opera);
        redirectAttributes.addFlashAttribute("success","Opera aggiunta al catalogo.");

        return "redirect:/Gestore/newOpera";
    }

    @GetMapping("/Gestore/newAsta")
    public String asta(Model model){

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();

        List<Opera> opere = opereRepository.findByStato(StatoOpera.DISPONIBILE);

        model.addAttribute("name", name);
        model.addAttribute("opere", opere);

        return "aggiungi_asta";
    }

    @PostMapping("/Gestore/newAsta")
    public String newAsta(@RequestParam @NotBlank String titolo,
                          @RequestParam("dataInizio") @Future Date start,
                          @RequestParam("dataFine") @Future Date end,
                          @RequestParam List<String> opere,
                          @RequestParam(required = false) String descrizione,
                          @RequestParam(required = false) MultipartFile file,
                          RedirectAttributes redirectAttributes) throws ParseException, IOException {

        Duration duration = Duration.between(start.toInstant(), end.toInstant());
        long hours = duration.toHours();

        if(hours < 3){
            redirectAttributes.addFlashAttribute("error","Durata minima di un'asta: 3 ore. " +
                    "Fissare un termine ad almeno 3 ore dopo la data d'inizio.");
        }

        else{
            String idGestore = authenticationService.getUser().getId();

            Asta asta = new Asta(titolo, idGestore, start, end);

            if(descrizione != null && !descrizione.isBlank()){
                asta.setDescrizione(descrizione);
            }

            if(!file.isEmpty()){
                asta.addPhoto(file);
            }

            asteRepository.save(asta);

            Opera opera;
            String idAsta = asta.getId();

            for (String idOpera : opere) {
                opera = opereRepository.findById(idOpera).orElseThrow();
                opera.setAsta(idAsta);
                opereRepository.save(opera);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
            String dataInizio = dateFormat.format(start);
            String oreInizio = hourFormat.format(start);
            String dataFine = dateFormat.format(end);
            String oreFine = hourFormat.format(end);

            redirectAttributes.addFlashAttribute("success","Asta aggiunta al catalogo. " +
                    "L'asta avrà inizio in data " + dataInizio + " alle ore " + oreInizio +
                    " e terminerà in data " + dataFine + " alle ore " + oreFine +".");
        }

        return "redirect:/Gestore/newAsta";
    }

}
