package com.heritart.dao;

import com.heritart.model.utenti.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UtentiRepository extends MongoRepository<Utente, String> {

    @Query("{email:'?0'}")
    Utente findByEmail(String email);

    @Query(value = "{email:'?0'}", exists = true)
    boolean isRegistered(String email);


}
