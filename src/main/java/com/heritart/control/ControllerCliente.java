package com.heritart.control;

import com.heritart.dao.OfferteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.offerte.Offerta;
import com.heritart.model.opere.*;
import com.heritart.model.utenti.Ruolo;
import com.heritart.model.utenti.Utente;
import com.heritart.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Iterator;
import java.util.List;

@Controller
public class ControllerCliente {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    OpereRepository opereRepository;
    @Autowired
    OfferteRepository offerteRepository;


    @GetMapping("/myCollection")
    public String collection(@RequestParam(defaultValue = "0") int page,
                             Model model){

        Utente utente = authenticationService.getUser();
        String name = utente.getNome();
        String email = utente.getEmail();
        Ruolo role = utente.getRuolo();

        List<Opera> opere = opereRepository.findByStato(StatoOpera.AGGIUDICATA);

        Iterator<Opera> iterator = opere.iterator();
        Opera opera; Offerta offerta; String idOpera;
        while (iterator.hasNext()){
            opera = iterator.next();
            idOpera = opera.getId();
            offerta = offerteRepository.findFirstByIdOperaOrderByValoreDesc(idOpera);
            if(!offerta.getEmail().equals(email)){
                iterator.remove();
            }
        }

        if(opere.isEmpty()){
            model.addAttribute("error", "Nessun opera aggiunta alla collezione.");
        }

        Pageable pageable = PageRequest.of(page, 8, Sort.Direction.ASC,"titolo");
        Page<Opera> operePages = new PageImpl<>(opere, pageable, opere.size());

        model.addAttribute("name", name);
        model.addAttribute("role", role.getName());
        model.addAttribute("opere", operePages);
        model.addAttribute("currentPage", page);

        return "collection";
    }

}
