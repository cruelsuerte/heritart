package com.heritart.control;

import com.heritart.dao.AsteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.opere.Opera;
import com.heritart.services.AuthenticationService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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

  @Autowired
  private AuthenticationService auth;

  @Autowired
  private AsteRepository asteRepository;

  @Autowired
  private OpereRepository opereRepository;





    @Test
    @WithMockUser(authorities = "GESTORE")
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
                .andExpect(status().is(302)) //stato della redirect
                    .andExpect(flash().attribute("success","Opera aggiunta al catalogo."));

    }


    @Test
    @WithMockUser(authorities = "GESTORE")
    void newOpera2() throws Exception {
//CASO 2 1 CAMPO NON è STATO INSERITO CORRETTAMENTE

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
    @WithMockUser(authorities = "GESTORE")
    void newOpera3() throws Exception {

//CASO 3 1 CAMPO OBBLIGATORIO NON è STATO INSERITO

        mvc.perform(post("/Gestore/newOpera")
                        .contentType("application/json")
                        .param("titolo","TITOLO")
                        .param("artista", "ARTISTA")
                        .param("anno","2000")
                        .param("provenienza","italia")
                        .param("tipologia","Dipinto")
                        //MANCA PROPRIETA'
                        .param("condizioni","NUOVA"))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error","Inserire proprieta."));


    }



    @Test
    @WithMockUser(authorities = "GESTORE")
    void newAsta1() throws Exception {
//CASO 1 CAMPI OBBLIGATORI INSERITI E CORRETTI
        auth.loadUserByUsername("vansmilton@gmail.com");

        mvc.perform(post("/Gestore/newAsta")
                        .contentType("application/json")
                        .param("titolo", "TITOLO")
                        .param("dataInizio", "2023-09-25T07:00")
                        .param("dataFine", "2023-09-26T07:00")
                        .param("opere", "64f73038436a6037d0b03550"))
                .andExpect(status().is(302)) //STATUS:REDIRECTION
                .andExpect(flash().attribute("success", "Asta aggiunta al catalogo. " +
                        "L'asta avrà inizio in data 25/09/2023 alle ore 07:00 e terminerà in data 26/09/2023 alle ore 07:00."));
    }

    @Test
    @WithMockUser(authorities = "GESTORE")
    void newAsta2() throws Exception {
//CASO 2 1 CAMPO NON è STATO INSERITO CORRETTAMENTE
        auth.loadUserByUsername("vansmilton@gmail.com");

        mvc.perform(post("/Gestore/newAsta")
                        .contentType("application/json")
                        .param("titolo", "TITOLO")
                        .param("dataInizio", "2023-09-25T07:00")
                        .param("dataFine", "sbagliato")
                        .param("opere", "64f73038436a6037d0b03550"))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error", "Campo non valido: dataFine."));
    }

    @Test
    @WithMockUser(authorities = "GESTORE")
    void newAsta3() throws Exception {
//CASO 3 1 CAMPO OBBLIGATORIO NON è STATO INSERITO

        mvc.perform(post("/Gestore/newAsta")
                        .contentType("application/json")
                        .param("titolo", "TITOLO")
                        .param("dataInizio", "2023-09-25T07:00")
                        //MANCA Datafine
                        .param("opere", "64f73038436a6037d0b03550"))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error", "Inserire dataFine."));
    }

    @Test
    @WithMockUser(authorities = "GESTORE")
    void newAsta4() throws Exception {
//CASO 4 durata asta minore di 3 ore

        mvc.perform(post("/Gestore/newAsta")
                        .contentType("application/json")
                        .param("titolo", "TITOLO")
                        .param("dataInizio", "2023-09-25T07:00")
                        .param("dataFine", "2023-09-25T08:00")
                        .param("opere", "64f73038436a6037d0b03550"))
                .andExpect(flash().attribute("error", "Durata minima di un'asta: 3 ore. Fissare un termine ad almeno 3 ore dopo la data d'inizio."));


    Asta asta = asteRepository.findByTitolo("TITOLO");
    asteRepository.deleteById(asta.getId());

    Opera opera=opereRepository.findByTitolo("TITOLO");
    opereRepository.delete(opera);

    }






}