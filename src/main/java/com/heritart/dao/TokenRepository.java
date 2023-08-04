package com.heritart.dao;

import com.heritart.model.Utente;
import com.heritart.model.VerificationToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<VerificationToken, String> {

    @Query("{email:'?0'}")
    VerificationToken findByEmail(String email);

}
