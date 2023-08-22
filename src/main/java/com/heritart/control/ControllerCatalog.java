package com.heritart.control;

import com.heritart.dao.AsteRepository;
import com.heritart.dao.OfferteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.aste.StatoAsta;
import com.heritart.model.offerte.Offerta;
import com.heritart.model.opere.Opera;
import com.heritart.model.utenti.Ruolo;
import com.heritart.model.utenti.Utente;
import com.heritart.utils.AuthenticationService;
import com.heritart.utils.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ControllerCatalog {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    OpereRepository opereRepository;
    @Autowired
    AsteRepository asteRepository;

    @GetMapping("/Home")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "") String search,
                       @RequestParam(required = false) StatoAsta stato,
                       Model model){
        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        Ruolo role = utente.getRuolo();

        List<Asta> aste = asteRepository.findByTitoloAndStato(search, stato);

        if(aste.isEmpty()){

            List<Opera> opere = opereRepository.findByTitoloOrArtistaOrCorrenteArtistica(search);

            String idAsta;
            Asta asta;

            for (Opera opera : opere) {
                idAsta = opera.getIdAsta();
                asta = asteRepository.findById(idAsta).orElseThrow();

                if(!aste.contains(asta)
                   && (stato == null || asta.getStato().equals(stato))){
                   aste.add(asta);
                }

            }


        }

        if(aste.isEmpty()){
            model.addAttribute("error", "Nessun risultato trovato.");
        }

        Pageable pageable = PageRequest.of(page, 8, Sort.Direction.DESC,"dataInizio");
        Page<Asta> astePages = new PageImpl<>(aste, pageable, aste.size());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        model.addAttribute("name", name);
        model.addAttribute("role", role.getName());
        model.addAttribute("currentPage", page);
        model.addAttribute("aste", astePages);
        model.addAttribute("dateFormat", dateFormat);

        return "home";
    }


    @GetMapping("/Catalog/{idAsta}")
    public String opere(@PathVariable String idAsta,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        Ruolo role = utente.getRuolo();

        Pageable pageable = PageRequest.of(page, 8, Sort.Direction.ASC,"titolo");
        Page<Opera> opere = opereRepository.findByIdAstaPages(idAsta, pageable);

        Asta asta = asteRepository.findById(idAsta).orElseThrow();
        StatoAsta stato = asta.getStato();

        model.addAttribute("name", name);
        model.addAttribute("role", role.getName());
        model.addAttribute("idAsta", idAsta);
        model.addAttribute("stato", stato.getName());
        model.addAttribute("opere", opere);
        model.addAttribute("currentPage", page);

        return "opere";
    }


}
