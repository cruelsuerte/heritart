package com.heritart.control;

import com.heritart.dao.AsteRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.aste.StatoAsta;
import com.heritart.model.opere.Tipologia;
import com.heritart.model.utenti.Utente;
import com.heritart.utils.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;

@Controller
public class ControllerCliente {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AsteRepository asteRepository;

    @GetMapping("/Cliente")
    public String cliente(@RequestParam(defaultValue = "0") int page,
                          Model model){

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        String role = utente.getRuolo().name();

        Page<Asta> aste = asteRepository.findAll(PageRequest.of(page, 8));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        model.addAttribute("name", name);
        model.addAttribute("role", role);
        model.addAttribute("aste", aste);
        model.addAttribute("currentPage", page);
        model.addAttribute("dateFormat", dateFormat);

        return "home";
    }

}
