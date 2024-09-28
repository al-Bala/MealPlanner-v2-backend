package com.mealplannerv2.diet;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "diets")
public class Diet {

    @Id
    private String id;

    private String name;
}
