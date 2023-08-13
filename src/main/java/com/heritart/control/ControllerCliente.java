package com.heritart.control;

import com.heritart.model.utenti.Utente;
import com.heritart.utils.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerCliente {

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/Cliente")
    public String cliente(Model model){
        Utente utente = authenticationService.getUser();
        String nome = utente.getNome();
        model.addAttribute("nome",nome);
        return "cliente";
    }


}
