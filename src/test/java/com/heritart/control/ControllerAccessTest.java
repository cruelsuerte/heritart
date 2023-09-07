package com.heritart.control;

import com.heritart.dao.OfferteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.dao.TokenRepository;
import com.heritart.dao.UtentiRepository;
import com.heritart.model.token.VerificationToken;
import com.heritart.model.utenti.Utente;
import com.heritart.services.AuthenticationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.Before;
import org.junit.After;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ControllerAccessTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    public AuthenticationService auth;
    @Autowired
    public UtentiRepository utentiRepository;

    @Autowired
    public TokenRepository tokenRepository;

    @Test
    void newCliente1() throws Exception{
        //CAMPO NON VALIDO
        mvc.perform(post("/newCliente")
                        .contentType("application/json")
                        .param("email-cl","email_errata")
                        .param("password-cl","password")
                        .param("nome-cl","Mario")
                        .param("cognome-cl","Rossi")
                        .param("paese-cl","Italia")
                        .param("citta-cl","Roma")
                        .param("cap-cl","81038")
                        .param("via-cl","Via Romaniello")
                        .param("tel-cl","331 76 26 628"))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error","Campo non valido: email."));


    }
    @Test
    void newCliente2() throws Exception{
        //1 CAMPO OBBLIGATORIO NON INSERITO
        mvc.perform(post("/newCliente")
                        .contentType("application/json")
                        //manca email
                        .param("password-cl","password")
                        .param("nome-cl","Walter")
                        .param("cognome-cl","White")
                        .param("paese-cl","Italia")
                        .param("citta-cl","Cosenza")
                        .param("cap-cl","81038")
                        .param("via-cl","Via Romaniello")
                        .param("tel-cl","331 75 26 628"))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error","Inserire email-cl."));
    }

    @Test
    void newCliente3() throws Exception {
        //TUTTI I CAMPI RISPETTATI
            mvc.perform(post("/newCliente")
                            .contentType("application/json")
                            .param("email-cl", "prova@example.com")
                            .param("password-cl", "prova")
                            .param("nome-cl", "Claudio")
                            .param("cognome-cl", "Rossi")
                            .param("paese-cl", "Italia")
                            .param("citta-cl", "Roma")
                            .param("cap-cl", "81038")
                            .param("via-cl", "Via Romaniello")
                            .param("tel-cl", "331 76 26 628"))
                    .andExpect(status().is(302))
                    .andExpect(flash().attribute("success", "Conferma la registrazione accedendo al link inviato a prova@example.com"));
        }





    @Test
    void newCliente4() throws Exception{
        //utente gia registrato
        mvc.perform(post("/newCliente")
                        .contentType("application/json")
                        .param("email-cl","prova@example.com")
                        .param("password-cl","prova")
                        .param("nome-cl","Claudio")
                        .param("cognome-cl","Rossi")
                        .param("paese-cl","Italia")
                        .param("citta-cl","Roma")
                        .param("cap-cl","81038")
                        .param("via-cl","Via Romaniello")
                        .param("tel-cl","331 76 26 628"))
                .andExpect(flash().attribute("error","L'utente prova@example.com è già registrato."));


        String idUtente=utentiRepository.findByEmail("prova@example.com").getId();
        utentiRepository.deleteById(idUtente);
        VerificationToken token = tokenRepository.findByEmail("prova@example.com");
        tokenRepository.deleteById(token.getId());


    }


}