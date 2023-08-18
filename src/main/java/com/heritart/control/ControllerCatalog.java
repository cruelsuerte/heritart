package com.heritart.control;

import com.heritart.dao.AsteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.aste.StatoAsta;
import com.heritart.model.opere.Opera;
import com.heritart.model.utenti.Utente;
import com.heritart.utils.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sound.midi.SysexMessage;
import java.text.SimpleDateFormat;

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
                          Model model){

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        String role = utente.getRuolo().getName();

        Pageable pageable = PageRequest.of(page, 8, Sort.Direction.DESC,"dataInizio");
        Page<Asta> aste = asteRepository.findAll(pageable);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        model.addAttribute("name", name);
        model.addAttribute("role", role);
        model.addAttribute("aste", aste);
        model.addAttribute("currentPage", page);
        model.addAttribute("dateFormat", dateFormat);

        return "home";
    }

    @GetMapping("/Catalog/{idAsta}")
    public String opere(@PathVariable String idAsta,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        String role = utente.getRuolo().getName();

        Pageable pageable = PageRequest.of(page, 8, Sort.Direction.ASC,"titolo");
        Page<Opera> opere = opereRepository.findByIdAsta(idAsta, pageable);

        Asta asta = asteRepository.findById(idAsta).orElseThrow();
        String stato = asta.getStato().getName();

        model.addAttribute("name", name);
        model.addAttribute("role", role);
        model.addAttribute("idAsta", idAsta);
        model.addAttribute("stato", stato);
        model.addAttribute("opere", opere);
        model.addAttribute("currentPage", page);

        return "opere";
    }


    @GetMapping("/Asta/{idOpera}")
    public String asta(@PathVariable String idOpera,
                        Model model) {

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        String role = utente.getRuolo().getName();

        Opera opera = opereRepository.findById(idOpera).orElseThrow();

        model.addAttribute("name", name);
        model.addAttribute("role", role);
        model.addAttribute("opera", opera);

        return "asta";
    }


}
