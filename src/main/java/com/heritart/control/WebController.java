package com.heritart.control;

import com.heritart.dao.UtentiRepository;
import com.heritart.model.Ruolo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController implements ErrorController {
    @Autowired
    UtentiRepository utentiRepository;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/";
    }

    @PostMapping ("/access")
        public String access() throws Exception{

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String e = auth.getPrincipal().toString();

            String ruolo = auth.getAuthorities().iterator().next().getAuthority();

            System.out.println(e + ruolo);

            if (ruolo == "GESTORE"){
                return "redirect:/Gestore";
            }

            else if (ruolo == "CLIENTE") {
                return "redirect:/Cliente";
            }

            else{
                return "redirect:/";
            }

    }

    @GetMapping("/Cliente")
    public String cliente(){
        return "cliente";
    }


    @GetMapping("/Gestore")
    public String gestore(){
        return "gestore";
    }

}
