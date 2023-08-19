package com.heritart.dao;

import com.heritart.model.offerte.Offerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OfferteRepository extends MongoRepository<Offerta, String> {

    @Query("{idOpera:'?0'}")
    Page<Offerta> findByIdOpera(String idOpera, Pageable pageable);


}
