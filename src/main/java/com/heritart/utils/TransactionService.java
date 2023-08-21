package com.heritart.utils;

import com.heritart.dao.OfferteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.dao.UtentiRepository;
import com.heritart.model.offerte.Offerta;
import com.heritart.model.opere.Opera;
import com.heritart.model.utenti.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TransactionService {
    @Autowired
    OpereRepository opereRepository;
    @Autowired
    OfferteRepository offerteRepository;

    public void makeOffer(String email, String idOpera, Integer value){

        Opera opera = opereRepository.findById(idOpera).orElseThrow();

        if(value >= (opera.getMinOfferta() + 50)){

            Offerta offerta = new Offerta(email, idOpera, value);
            offerteRepository.save(offerta);

            opera.setMinOfferta(value);
            opereRepository.save(opera);

        }

    }

}
