package com.heritart.dao;

import com.heritart.model.Opera.Opera;
import com.heritart.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface OpereRepository extends MongoRepository<Opera, String> {

    @Query("{titolo:'?0'}")
    Opera findByTitolo(String titolo);

}
