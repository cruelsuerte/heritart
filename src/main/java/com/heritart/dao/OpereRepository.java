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
    @Query("{acquirente:'?0', stato:'AGGIUDICATA'}")
    List<Opera> findByAcquirente(String acquirente);
    @Query("{idAsta:'?0'}")
    List<Opera> findByIdAsta(String idAsta);

    @Query("{idAsta:'?0'}")
    Page<Opera> findByIdAstaPages(String idAsta, Pageable pageable);

    @Query(value="{$or :[{titolo: {$regex : ?0, $options: 'i'}}," +
                        "{artista: {$regex : ?0, $options: 'i'}}," +
                        "{correnteArtistica: {$regex : ?0, $options: 'i'}}]}")
    List<Opera> findByTitoloOrArtistaOrCorrenteArtistica(String search);

}
