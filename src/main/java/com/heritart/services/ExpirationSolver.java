package com.heritart.services;

import com.heritart.dao.AsteRepository;
import com.heritart.dao.OfferteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.dao.TokenRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.aste.StatoAsta;
import com.heritart.model.offerte.Offerta;
import com.heritart.model.opere.Opera;
import com.heritart.model.opere.StatoOpera;
import com.heritart.model.token.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ExpirationSolver {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    OpereRepository opereRepository;
    @Autowired
    AsteRepository asteRepository;

    @Autowired
    OfferteRepository offerteRepository;

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void checkAste(){

        List<Asta> aste = asteRepository.findAll();

        for (Asta asta : aste) {

            if(asta.isInCorso()){
                asta.setStato(StatoAsta.IN_CORSO);
                asteRepository.save(asta);
            }

            if(asta.isTerminata()){
                asta.setStato(StatoAsta.TERMINATA);
                asteRepository.save(asta);

                String idAsta = asta.getId();
                String titoloAsta = asta.getTitolo();
                List<Opera> opere = opereRepository.findByIdAsta(idAsta);

                for (Opera opera : opere) {
                    String idOpera = opera.getId();
                    Offerta bestOffer = offerteRepository.findFirstByIdOperaOrderByValoreDesc(idOpera);

                    if(bestOffer != null){
                        String titoloOpera = opera.getTitolo();
                        String email = bestOffer.getEmail();
                        Integer valore = bestOffer.getValore();

                        opera.setOfferta(valore);
                        opera.setStato(StatoOpera.AGGIUDICATA);

                        newSale(titoloAsta, titoloOpera, email, valore);
                    }

                    else{
                        opera.setStato(StatoOpera.DISPONIBILE);
                    }

                    opereRepository.save(opera);
                }

            }

        }

    }

    @Scheduled(fixedDelay = 60000, initialDelay = 60000)
    public void checkVerificationToken(){

        List<VerificationToken> tokenList = tokenRepository.findAll();

        for (VerificationToken token : tokenList) {
            if(token.isExpired()){
                String idToken = token.getId();
                tokenRepository.deleteById(idToken);
            }

        }

    }

    public void newSale(String asta, String opera, String email, Integer valore){
        MailSender mailSender = new MailSender("heritart.noreply@gmail.com","smtp.gmail.com","axqoblnhehpubpbg");
        mailSender.send(email,"Opera aggiudicata",
                "Congratulazioni, ti sei aggiudicato l'opera " + opera + " messa all'asta " + asta + " presentando la migliore offerta: " +
                      valore + " €. Puoi procedere con il pagamento, a presto.");
    }


}
