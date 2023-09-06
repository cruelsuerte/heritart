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
import com.heritart.services.AuthenticationService;
import com.heritart.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ControllerAsta {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    OpereRepository opereRepository;
    @Autowired
    AsteRepository asteRepository;
    @Autowired
    OfferteRepository offerteRepository;
    @Autowired
    TransactionService transactionService;

    @GetMapping("/Catalog/{idAsta}/Asta/{idOpera}")
    public String asta(@PathVariable String idAsta,
                       @PathVariable String idOpera,
                       Model model) {

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        Ruolo role = utente.getRuolo();

        Asta asta = asteRepository.findById(idAsta).orElseThrow();
        StatoAsta stato = asta.getStato();

        if(stato != StatoAsta.PROGRAMMATA){

            Opera opera = opereRepository.findById(idOpera).orElseThrow();

            List<Offerta> offerte = offerteRepository.findFirst10ByIdOperaOrderByValoreDesc(idOpera);

            Date dataFine = asta.getDataFine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            model.addAttribute("name", name);
            model.addAttribute("role", role.getName());
            model.addAttribute("stato", stato.getName());
            model.addAttribute("opera", opera);
            model.addAttribute("offerte", offerte);
            model.addAttribute("dataFine", dataFine);
            model.addAttribute("dateFormat", dateFormat);

            return "asta";
        }

        else{
            return "redirect:/Catalog/" + idAsta;
        }

    }



    @PostMapping("/Catalog/{idAsta}/Asta/{idOpera}")
    public String offerta(@PathVariable String idAsta,
                          @PathVariable String idOpera,
                          @RequestParam Integer value,
                          RedirectAttributes redirectAttributes) {

        Utente utente = authenticationService.getUser();
        Ruolo role = utente.getRuolo();

        Asta asta = asteRepository.findById(idAsta).orElseThrow();

        if(asta.getStato() == StatoAsta.IN_CORSO
           && role == Ruolo.CLIENTE
        ){

            String email = utente.getEmail();
            Offerta lastOffer = offerteRepository.findFirstByIdOperaOrderByValoreDesc(idOpera);

            if(lastOffer == null || !email.equals(lastOffer.getEmail())){
            try {
                transactionService.makeOffer(email, idOpera, value);
                redirectAttributes.addFlashAttribute("success",
                        "Nuova offerta presentata con successo.");
            }
            catch (Exception e){
                redirectAttributes.addFlashAttribute("error",
                        "Offerta presentata non valida. Rinvia una nuova offerta.");
            }
            }

            else{
                redirectAttributes.addFlashAttribute("error",
                        "Hai già presentato l'ultima migliore offerta. Attendi una nuova offerta.");
            }


        }

        return "redirect:/Catalog/" + idAsta + "/Asta/" + idOpera;

    }


}
