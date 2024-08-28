package com.mealplannerv2.auth.token;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tokens")
public class Token {
    @Id
    private ObjectId id;

    private String token;

    @Field("token_type")
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @Field("username")
    private String username;

    public Token(String token, String username, TokenType tokenType) {
        this.token = token;
        this.username = username;
        this.tokenType = tokenType;
        this.expired = false;
        this.revoked = false;
    }
}
