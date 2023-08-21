package com.heritart.dao;

import com.heritart.model.offerte.Offerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferteRepository extends MongoRepository<Offerta, String> {

    @Query("{idOpera:'?0'}")
    Page<Offerta> findByIdOpera(String idOpera, Pageable pageable);

    List<Offerta> findFirst10ByIdOperaOrderByValoreDesc(String idOpera);
    Offerta findFirstByIdOperaOrderByValoreDesc(String idOpera);

}
