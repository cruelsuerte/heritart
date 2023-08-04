package com.heritart.control;

import com.heritart.MailSender;
import com.heritart.dao.UtentiRepository;
import com.heritart.model.Ruolo;
import com.heritart.model.Utente;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController implements ErrorController {
    @Autowired
    UtentiRepository utentiRepository;

    @GetMapping("/Home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/Home";
    }

    @PostMapping("/Home/login")
    public String login(RedirectAttributes redirectAttributes) throws Exception{

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
            return "redirect:/Home";
        }

    }

    @PostMapping("/Home/loginFailure")
    public String loginFailure(RedirectAttributes redirectAttributes) throws Exception{

        redirectAttributes.addFlashAttribute("message", "Credenziali non valide.");
        redirectAttributes.addFlashAttribute("error",true);

        return "redirect:/Home";

    }

    @PostMapping("/Home/newCliente")
    public String newCliente(@RequestParam("email-cl") String email,
                             @RequestParam("password-cl") String password,
                             @RequestParam("nome-cl") String nome,
                             @RequestParam("cognome-cl") String cognome,
                             @RequestParam("paese-cl") String paese,
                             @RequestParam("citta-cl") String citta,
                             @RequestParam("cap-cl") String cap,
                             @RequestParam("via-cl") String via,
                             @RequestParam("tel-cl") String telefono,
                             RedirectAttributes redirectAttributes){

        if (utentiRepository.isRegistered(email)){
            redirectAttributes.addFlashAttribute("message", "L'utente " + email + " è già registrato.");
            redirectAttributes.addFlashAttribute("error",true);
        }

        else {
            Utente utente = new Utente(email, new BCryptPasswordEncoder().encode(password), nome, cognome, Ruolo.CLIENTE);
            String indirizzo = via + ", " + citta + ", " + paese + ", " + cap;
            utente.setIndirizzo(indirizzo);
            utente.setTelefono(telefono);
            utentiRepository.save(utente);
            confirmRegistration(utente, redirectAttributes);
        }

        return "redirect:/Home";
    }


    @PostMapping("/Home/newGestore")
    public String newGestore(@RequestParam("email-ge") String email,
                             @RequestParam("password-ge") String password,
                             @RequestParam("nome-ge") String nome,
                             @RequestParam("cognome-ge") String cognome,
                             RedirectAttributes redirectAttributes){

        if (utentiRepository.isRegistered(email)){
            redirectAttributes.addFlashAttribute("message", "L'utente " + email + " è già registrato");
            redirectAttributes.addFlashAttribute("error",true);
        }

        else {
            Utente utente = new Utente(email, new BCryptPasswordEncoder().encode(password), nome, cognome, Ruolo.GESTORE);
            utentiRepository.save(utente);
            confirmRegistration(utente, redirectAttributes);
        }

        return "redirect:/Home";
    }


    @GetMapping("/Cliente")
    public String cliente(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Utente utente = utentiRepository.findByEmail(email);
        model.addAttribute("utente",utente);
        return "cliente";
    }


    @GetMapping("/Gestore")
    public String gestore(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Utente utente = utentiRepository.findByEmail(email);
        model.addAttribute("utente",utente);
        return "gestore";
    }

    public void confirmRegistration(Utente utente, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "Registrazione effettuata");
        redirectAttributes.addFlashAttribute("error",false);
        MailSender mailSender = new MailSender("heritart.noreply@gmail.com","smtp.gmail.com","axqoblnhehpubpbg");
        mailSender.send(utente.getEmail(),"Conferma registrazione",
                "Ciao " + utente.getNome() + ", grazie per esserti registrato a HeritArt come " + utente.getRuolo().name()
                        + ". Puoi confermare la tua registrazione a questo link: http://localhost:8080/token, a presto.");
    }


}
