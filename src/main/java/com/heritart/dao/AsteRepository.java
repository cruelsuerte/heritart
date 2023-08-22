package com.heritart.dao;

import com.heritart.model.aste.Asta;
import com.heritart.model.aste.StatoAsta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AsteRepository extends MongoRepository<Asta, String> {

    @Query("{titolo:'?0'}")
    Asta findByTitolo(String titolo);

    @Query("{stato:'?0'}")
    List<Asta> findByStato(StatoAsta stato);

    @Query(value="{$and: [{ $or : [ { $expr: { $eq: ['?0', ''] } } , { titolo : {$regex : ?0, $options: 'i'} } ] }, { $or : [ { $expr: { $eq: ['?1', 'null'] } } , { stato : ?1 } ] } ]}")
    List<Asta> findByTitoloAndStato(String titolo, StatoAsta stato);



}