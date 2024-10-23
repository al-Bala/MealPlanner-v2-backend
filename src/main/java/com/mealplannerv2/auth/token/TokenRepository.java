package com.mealplannerv2.auth.token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    @Query("{ 'username': ?0, $or: [ { 'expired': false }, { 'revoked': false } ] }")
    List<Token> findAllValidTokensByUser(String username);

    @Query("{username:  ?0, $or:[{expired: false}, {revoked: false}], token_type: 'REFRESH'}")
    Optional<Token> findValidRefreshToken(String username);

    List<Token> findByToken(String token);
}
