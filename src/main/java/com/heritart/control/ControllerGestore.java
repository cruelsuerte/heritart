package com.heritart.control;

import com.heritart.utils.AuthenticationService;
import com.heritart.dao.AsteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.opere.*;
import com.heritart.model.utenti.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
public class ControllerGestore {

    @Autowired
    OpereRepository opereRepository;

    @Autowired
    AsteRepository asteRepository;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/Gestore")
    public String gestore(Model model){
        Utente utente = authenticationService.getUser();
        String nome = utente.getNome();
        model.addAttribute("nome",nome);
        return "gestore";
    }

    @GetMapping("/Gestore/newOpera")
    public String opera(Model model){
        model.addAttribute("tipologiaList", Tipologia.values());
        model.addAttribute("condizioniList", Condizioni.values());
        model.addAttribute("tecnicaList", Tecnica.values());
        model.addAttribute("correnteArtisticaList", CorrenteArtistica.values());
        model.addAttribute("materialeList", Materiale.values());
        model.addAttribute("proprietaList", Proprieta.values());
        return "aggiungi_opera";
    }


    @PostMapping("/Gestore/newOpera")
    public String newOpera(@RequestParam("titolo") String titolo,
                           @RequestParam("artista") String artista,
                           @RequestParam("annoCreazione") int annoCreazione,
                           @RequestParam("provenienza") String provenienza,
                           @RequestParam("tipologia") Tipologia tipologia,
                           @RequestParam("proprieta") Proprieta proprieta,
                           @RequestParam("condizioni") Condizioni condizioni,
                           @RequestParam("dim1") int dim1,
                           @RequestParam("dim2") int dim2,
                           @RequestParam(name = "dim3", defaultValue = "0") int dim3,
                           @RequestParam(name = "descrizione",required = false) String descrizione,
                           @RequestParam(name = "correnteArtistica", required = false) CorrenteArtistica correnteArtistica,
                           @RequestParam(name = "tecnica",required = false) Tecnica tecnica,
                           @RequestParam(name = "materiale", required = false) Materiale materiale,
                           @RequestParam(name = "baseAsta",defaultValue = "0") int baseAsta,
                           @RequestParam(name = "files", required = false) MultipartFile[] files,
                           RedirectAttributes redirectAttributes) throws ParseException, IOException {

        String dimensioni = dim1 + " x " + dim2;
        if(dim3 != 0) {
            dimensioni = descrizione + " x " + dim3;
        }

        Opera opera = new Opera(titolo, artista, annoCreazione, provenienza, tipologia, dimensioni, proprieta, condizioni);

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

        if(baseAsta != 0){
            opera.setBaseAsta(baseAsta);
        }

        if(files.length != 0){
            for (MultipartFile file : files) {
                opera.addPhoto(file);
            }
        }

        opereRepository.save(opera);

        redirectAttributes.addFlashAttribute("success",true);

        return "redirect:/Gestore/newOpera";
    }

    @GetMapping("/Gestore/newAsta")
    public String aste(Model model){

        List<Opera> opere = opereRepository.findByStato(StatoOpera.DISPONIBILE);

        model.addAttribute("opere", opere);

        return "aggiungi_asta";
    }

    @PostMapping("/Gestore/newAsta")
    public String newAsta(@RequestParam("titolo") String titolo,
                          @RequestParam("dataInizio") String dataInizio,
                          @RequestParam("dataFine") String dataFine,
                          @RequestParam("opere") List<String> opere,
                          @RequestParam(name = "descrizione",required = false) String descrizione,
                          @RequestParam(name = "file", required = false) MultipartFile file,
                          RedirectAttributes redirectAttributes) throws ParseException, IOException {

        String idGestore = authenticationService.getUser().getId();

        Asta asta = new Asta(titolo, idGestore);

        asta.setDataInizio(dataInizio);
        asta.setDataFine(dataFine);

        if(descrizione != null && !descrizione.isBlank()){
            asta.setDescrizione(descrizione);
        }

        if(!file.isEmpty()){
            asta.addPhoto(file);
        }

        if (!opere.isEmpty()){

            asteRepository.save(asta);

            Opera opera;
            String idAsta = asta.getId();

            for (String idOpera : opere) {
                opera = opereRepository.findById(idOpera).orElseThrow();
                opera.setAsta(idAsta);
                opereRepository.save(opera);
            }

            redirectAttributes.addFlashAttribute("success",true);
        }

        return "redirect:/Gestore/newAsta";
    }

}
