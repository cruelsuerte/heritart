package com.heritart.control;

import com.heritart.dao.OfferteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.offerte.Offerta;
import com.heritart.model.opere.Opera;
import com.heritart.services.AuthenticationService;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ControllerAstaTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthenticationService auth;
    @Autowired
    private OpereRepository opereRepository;

    @Autowired
    private OfferteRepository offerteRepository;




    @Test
    @WithMockUser(username = "lucacampanile3@gmail.com", password = "luca", authorities = "CLIENTE")
    void Offerta1()throws Exception{
        //TEST-1 OFFERTA PIU BASSA DELL'ULTIMA MIGLIOR PRESENTATA
        mvc.perform(post("/Catalog/64dd38bd6ae17b75c8b91544/Asta/64f7303c436a6037d0b03551")
                        .param("value","7000"))
                .andExpect(status().is(302)) //STATUS:REDIRECTION
                .andExpect(flash().attribute("error", "Offerta presentata non valida. Rinvia una nuova offerta."));


    }

    @Test
    @WithMockUser(username = "lucacampanile3@gmail.com", password = "luca", authorities = "CLIENTE")
    void Offerta2()throws Exception{
        //TEST-2 OFFERTA PRESENTATA CORRETTAMENTE
        mvc.perform(post("/Catalog/64dd38bd6ae17b75c8b91544/Asta/64f7303c436a6037d0b03551")
                .param("value","16000"))
                .andExpect(status().is(302)) //STATUS:REDIRECTION
                .andExpect(flash().attribute("success", "Nuova offerta presentata con successo."));
    }


    //TEST-3 GIA PRESENTATA ULTIMA MIGLIORE OFFERTA 
    @Test
    @WithMockUser(username = "lucacampanile3@gmail.com", password = "luca", authorities = "CLIENTE")
    void Offerta3()throws Exception{

        mvc.perform(post("/Catalog/64dd38bd6ae17b75c8b91544/Asta/64f7303c436a6037d0b03551")
                        .param("value","11000"))
                .andExpect(status().is(302)) //STATUS:REDIRECTION
                .andExpect(flash().attribute("error", "Hai già presentato l'ultima migliore offerta. Attendi una nuova offerta."));

        Opera opera=opereRepository.findById("64f7303c436a6037d0b03551").orElseThrow();
        opera.setOfferta(8000);
        opereRepository.save(opera);
        Offerta offerta=offerteRepository.findFirstByIdOperaOrderByValoreDesc("64f7303c436a6037d0b03551");
        offerteRepository.delete(offerta);

    }








}


