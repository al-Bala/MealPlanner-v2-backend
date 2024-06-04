package com.mealplannerv2.loginandregister;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginAndRegisterRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
