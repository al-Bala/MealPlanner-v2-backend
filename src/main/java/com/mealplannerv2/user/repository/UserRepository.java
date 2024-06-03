package com.mealplannerv2.user.repository;

import com.mealplannerv2.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByLogin(String login);
}
