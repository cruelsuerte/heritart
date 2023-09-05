package com.heritart.control;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ControllerGestoreTest {

   @Autowired
   private MockMvc mvc;

//@MockBean se mi serve qualche service o repo

    @Test
    void newOpera1() throws Exception {
//CASO 1 TUTTI I CAMPI OBBLIGATORI SONO INSERITI CORRETTAMENTE

        mvc.perform(post("/Gestore/newOpera")
                .contentType("application/json")
                .param("titolo","TITOLO")
                .param("artista", "ARTISTA")
                        .param("anno","2000")
                        .param("provenienza","italia")
                        .param("tipologia","Dipinto")
                .param("proprieta","COLLEZIONE_PRIVATA")
                        .param("condizioni","NUOVA"))
                .andExpect(status().isOk()); //stato della redirect


    }


    @Test
    void newOpera2() throws Exception {
;
//CASO 1 TUTTI I CAMPI OBBLIGATORI SONO INSERITI CORRETTAMENTE

        mvc.perform(post("/Gestore/newOpera")
                        .contentType("application/json")
                        .param("titolo","TITOLO")
                        .param("artista", "ARTISTA")
                        .param("anno","2000")
                        .param("provenienza","italia")
                        .param("tipologia","Dipinto")
                        .param("proprieta","COLLEZIONE_PRIVATA")
                        .param("condizioni","NUOVA")
                        .param("dim1","stringa"))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error","Campo non valido: dim1."));


    }



    @Test
    void newAsta() {

    }
}