package com.mealplannerv2.auth.token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {

    @Query("{username:  ?0, $or:[{expired: false}, {revoked: false}]}")
    List<Token> findAllValidTokensByUser(String username);

    Optional<Token> findByToken(String token);
}
