package com.mealplannerv2.diet;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DietRepository extends MongoRepository<Diet, String> {
}
