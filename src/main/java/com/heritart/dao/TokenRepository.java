package com.heritart.dao;

import com.heritart.model.token.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TokenRepository extends MongoRepository<VerificationToken, String> {

    @Query("{email:'?0'}")
    VerificationToken findByEmail(String email);

}
