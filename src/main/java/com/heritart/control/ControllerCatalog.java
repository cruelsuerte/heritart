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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class ControllerCatalog {

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

    @GetMapping("/Home")
    public String home(@RequestParam(defaultValue = "0") int page,
                          Model model){

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        Ruolo role = utente.getRuolo();

        Pageable pageable = PageRequest.of(page, 8, Sort.Direction.DESC,"dataInizio");
        Page<Asta> aste = asteRepository.findAll(pageable);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        model.addAttribute("name", name);
        model.addAttribute("role", role.getName());
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


    @GetMapping("/Catalog/{idAsta}/Asta/{idOpera}")
    public String asta(@PathVariable String idAsta,
                       @PathVariable String idOpera,
                        Model model) {

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        Ruolo role = utente.getRuolo();

        Asta asta = asteRepository.findById(idAsta).orElseThrow();

        if(asta.getStato() == StatoAsta.IN_CORSO){

            Opera opera = opereRepository.findById(idOpera).orElseThrow();

            //Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,"valore");
            //Page<Offerta> offerte = offerteRepository.findByIdOpera(idOpera, pageable);

            List<Offerta> offerte = offerteRepository.findFirst10ByIdOperaOrderByValoreDesc(idOpera);

            Date dataFine = asta.getDataFine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

            model.addAttribute("name", name);
            model.addAttribute("role", role.getName());
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
//           && role == Ruolo.CLIENTE
           ){

            String email = utente.getEmail();
            Offerta lastOffer = offerteRepository.findFirstByIdOperaOrderByValoreDesc(idOpera);

//            if(lastOffer == null || !email.equals(lastOffer.getEmail())){
                try {
                    transactionService.makeOffer(email, idOpera, value);
                    redirectAttributes.addFlashAttribute("success",
                            "Nuova offerta presentata con successo.");
                }
                catch (Exception e){
                    redirectAttributes.addFlashAttribute("error",
                            "Una nuova offerta è stata presentata durante la tua richiesta. Rinvia una nuova offerta.");
                }
//            }
//
//            else{
//                redirectAttributes.addFlashAttribute("error",
//                        "Hai già presentato l'ultima migliore offerta. Attendi una nuova offerta.");
//            }


            return "redirect:/Catalog/" + idAsta + "/Asta/" + idOpera;
        }

        else{
            return "redirect:/Catalog/" + idAsta;
        }

    }



}
