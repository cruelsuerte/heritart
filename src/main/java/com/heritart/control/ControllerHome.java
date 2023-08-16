package com.heritart.control;

import com.heritart.utils.AuthenticationService;
import com.heritart.utils.MailSender;
import com.heritart.dao.OpereRepository;
import com.heritart.dao.TokenRepository;
import com.heritart.dao.UtentiRepository;
import com.heritart.model.utenti.Ruolo;
import com.heritart.model.utenti.Utente;
import com.heritart.model.token.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.NoSuchElementException;

@Controller
@Validated
public class ControllerHome implements ErrorController {
    @Autowired
    UtentiRepository utentiRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/Home")
    public String home(Model model) {
        return "login";
    }

    @GetMapping("/error")
    public String error() {
        System.out.println("ERRORE");
        return "redirect:/Home";
    }

    @PostMapping("/Home/login")
    public String login() {
        System.out.println("ACCESSO EFFETTUATO");
        return "redirect:/Home/access";
    }

    @GetMapping("/Home/access")
    public String access() {

        Utente utente = authenticationService.getUser();
        String ruolo = utente.getRuolo().name();

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
    public String loginFailure(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               RedirectAttributes redirectAttributes){

        Utente utente = utentiRepository.findByEmail(email);
        BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

        if (utente != null && bCryptEncoder.matches(password,utente.getPassword()) && !utente.isEnabled()){
            redirectAttributes.addFlashAttribute("error", "Registrazione di " + utente.getEmail() + " non confermata.");
            redirectAttributes.addFlashAttribute("resendId", utente.getId());

        }

        else {
            redirectAttributes.addFlashAttribute("error", "Autenticazione fallita.");
        }

        return "redirect:/Home";

    }

    @PostMapping("/Home/newCliente")
    public String newCliente(@RequestParam("email-cl") @Email String email,
                             @RequestParam("password-cl") @NotBlank String password,
                             @RequestParam("nome-cl") @NotBlank String nome,
                             @RequestParam("cognome-cl") @NotBlank String cognome,
                             @RequestParam("paese-cl") @NotBlank String paese,
                             @RequestParam("citta-cl") @NotBlank String citta,
                             @RequestParam("cap-cl") @NotBlank String cap,
                             @RequestParam("via-cl") @NotBlank String via,
                             @RequestParam("tel-cl") @NotBlank String telefono,
                             RedirectAttributes redirectAttributes){

        if (utentiRepository.isRegistered(email)){
            redirectAttributes.addFlashAttribute("error", "L'utente " + email + " è già registrato.");
        }

        else {
            Utente utente = new Utente(email, new BCryptPasswordEncoder().encode(password), nome, cognome, Ruolo.CLIENTE);
            String indirizzo = via + ", " + citta + ", " + paese + ", " + cap;
            utente.setIndirizzo(indirizzo);
            utente.setTelefono(telefono);
            utentiRepository.save(utente);
            newRegistration(email, redirectAttributes);
        }

        return "redirect:/Home";
    }


    @PostMapping("/Home/newGestore")
    public String newGestore(@RequestParam("email-ge") @Email String email,
                             @RequestParam("password-ge") @NotBlank String password,
                             @RequestParam("nome-ge") @NotBlank String nome,
                             @RequestParam("cognome-ge") @NotBlank String cognome,
                             RedirectAttributes redirectAttributes){

        if (utentiRepository.isRegistered(email)){
            redirectAttributes.addFlashAttribute("error", "L'utente " + email + " è già registrato.");
        }

        else {
            Utente utente = new Utente(email, new BCryptPasswordEncoder().encode(password), nome, cognome, Ruolo.GESTORE);
            utentiRepository.save(utente);
            newRegistration(email, redirectAttributes);
        }

        return "redirect:/Home";
    }

    @GetMapping("/Home/Confirm/{idToken}")
    public String confirm(@PathVariable String idToken,
                          RedirectAttributes redirectAttributes){

        VerificationToken token = tokenRepository.findById(idToken).orElseThrow();
        String email = token.getEmail();
        Utente utente = utentiRepository.findByEmail(email);

        if (utente != null && !utente.isEnabled()){

            if(token != null && !token.isExpired()){
                utente.setEnabled(true);
                utentiRepository.save(utente);
                tokenRepository.deleteById(idToken);

                redirectAttributes.addFlashAttribute("success", "Registrazione di " + email + " confermata.");
            }

            else {
                redirectAttributes.addFlashAttribute("error", "Link di conferma scaduto.");
                redirectAttributes.addFlashAttribute("resendId", utente.getId());

            }

        }

        return "redirect:/Home";
    }

    @GetMapping("/Home/Resend/{idUser}")
    public String resend(@PathVariable String idUser,
                         RedirectAttributes redirectAttributes){

        Utente utente = utentiRepository.findById(idUser).orElseThrow();

        if (utente != null && !utente.isEnabled()){

            String email = utente.getEmail();
            VerificationToken token = tokenRepository.findByEmail(email);

            if(token != null) {
                String idToken = token.getId();
                tokenRepository.deleteById(idToken);
            }

            newRegistration(email, redirectAttributes);
        }

        return "redirect:/Home";
    }


    public void newRegistration(String email, RedirectAttributes redirectAttributes){

        VerificationToken token = new VerificationToken(email);
        token.setExpiration(1);
        tokenRepository.save(token);

        redirectAttributes.addFlashAttribute("success", "Conferma la registrazione accedendo al link inviato a " + email);
        MailSender mailSender = new MailSender("heritart.noreply@gmail.com","smtp.gmail.com","axqoblnhehpubpbg");
        mailSender.send(email,"Conferma registrazione",
                "Ciao, grazie per esserti registrato a HeritArt come. Puoi confermare la tua registrazione a questo link:" +
                        " http://localhost:8080/Home/Confirm/" + token.getId() + ", a presto.");
    }

}
