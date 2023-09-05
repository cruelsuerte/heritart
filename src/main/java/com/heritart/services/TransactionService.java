package com.heritart.services;

import com.heritart.dao.OfferteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.offerte.Offerta;
import com.heritart.model.opere.Opera;
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

    public void makeOffer(String email, String idOpera, Integer value) throws Exception{

        Opera opera = opereRepository.findById(idOpera).orElseThrow();

        if(value >= opera.getOfferta()){

            Offerta offerta = new Offerta(email, idOpera, value);
            offerteRepository.save(offerta);

            opera.setOfferta(value + 50);
            opereRepository.save(opera);

        }

        else{
            throw new Exception();
        }

    }

}
