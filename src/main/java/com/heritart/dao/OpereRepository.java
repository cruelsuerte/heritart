package com.heritart.dao;

import com.heritart.model.opere.Opera;
import com.heritart.model.opere.StatoOpera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OpereRepository extends MongoRepository<Opera, String> {

    @Query("{titolo:'?0'}")
    Opera findByTitolo(String titolo);

    @Query("{stato:'?0'}")
    List<Opera> findByStato(StatoOpera stato);

    @Query("{idAsta:'?0', stato: 'ASTA'}")
    Page<Opera> findByIdAsta(String idAsta, Pageable pageable);

}
