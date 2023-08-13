package com.heritart.dao;

import com.heritart.model.aste.Asta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AsteRepository extends MongoRepository<Asta, String> {

    @Query("{titolo:'?0'}")
    Asta findByTitolo(String titolo);

}