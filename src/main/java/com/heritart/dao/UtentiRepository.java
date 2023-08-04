package com.heritart.dao;

import com.heritart.model.Utente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtentiRepository extends MongoRepository<Utente, String> {

    @Query("{email:'?0'}")
    Utente findByEmail(String email);

    @Query(value = "{email:'?0'}", exists = true)
    boolean isRegistered(String email);


}
