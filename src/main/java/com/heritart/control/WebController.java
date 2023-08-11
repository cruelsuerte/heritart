package com.heritart.control;

import com.heritart.AuthenticationService;
import com.heritart.MailSender;
import com.heritart.dao.OpereRepository;
import com.heritart.dao.TokenRepository;
import com.heritart.dao.UtentiRepository;
import com.heritart.model.Opera.*;
import com.heritart.model.Ruolo;
import com.heritart.model.Utente;
import com.heritart.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;

@Controller
public class WebController implements ErrorController {
    @Autowired
    UtentiRepository utentiRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    OpereRepository opereRepository;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/Home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/Home";
    }

    @PostMapping("/Home/login")
    public String login() throws Exception{

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
            redirectAttributes.addFlashAttribute("message", "Registrazione di " + utente.getEmail() + " non confermata.");
            redirectAttributes.addFlashAttribute("resendId", utente.getId());

        }

        else {
            redirectAttributes.addFlashAttribute("message", "Autenticazione fallita.");
        }

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
            newRegistration(email, redirectAttributes);
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
            redirectAttributes.addFlashAttribute("message", "L'utente " + email + " è già registrato.");
            redirectAttributes.addFlashAttribute("error",true);
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

                redirectAttributes.addFlashAttribute("message", "Registrazione di " + email + " confermata.");
                redirectAttributes.addFlashAttribute("error",false);
            }

            else {
                redirectAttributes.addFlashAttribute("message", "Link di conferma scaduto.");
                redirectAttributes.addFlashAttribute("resendId", utente.getId());
                redirectAttributes.addFlashAttribute("error",true);
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


    @GetMapping("/Cliente")
    public String cliente(Model model){
        Utente utente = authenticationService.getUser();
        model.addAttribute("utente",utente);
        return "cliente";
    }


    @GetMapping("/Gestore")
    public String gestore(Model model){
        Utente utente = authenticationService.getUser();
        model.addAttribute("utente",utente);
        return "gestore";
    }

    @GetMapping("/Gestore/newOpera")
    public String opera(Model model){
        model.addAttribute("tipologiaList",Tipologia.values());
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
                           @RequestParam(name = "prezzoMinimo",defaultValue = "0") int prezzoMinimo,
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

        if(prezzoMinimo != 0){
            opera.setPrezzoMinimo(prezzoMinimo);
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

    public void newRegistration(String email, RedirectAttributes redirectAttributes){

        VerificationToken token = new VerificationToken(email);
        token.setExpiration(1);
        tokenRepository.save(token);

        redirectAttributes.addFlashAttribute("message", "Conferma la registrazione accedendo al link inviato a " + email);
        redirectAttributes.addFlashAttribute("error",false);
        MailSender mailSender = new MailSender("heritart.noreply@gmail.com","smtp.gmail.com","axqoblnhehpubpbg");
        mailSender.send(email,"Conferma registrazione",
                "Ciao, grazie per esserti registrato a HeritArt come. Puoi confermare la tua registrazione a questo link:" +
                        " http://localhost:8080/Home/Confirm/" + token.getId() + ", a presto.");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String NoSuchElementException() {
        return "redirect:/error";
    }

}
