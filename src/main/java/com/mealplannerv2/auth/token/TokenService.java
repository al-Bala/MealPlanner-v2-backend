package com.mealplannerv2.auth.token;

import com.mealplannerv2.auth.infrastructure.controller.dto.LoginTokens;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TokenService {

    final private TokenRepository tokenRepository;

    public void updateAllTokens(LoginTokens loginTokens) {
        revokeAllUserTokens(loginTokens.userId());
        tokenRepository.saveAll(List.of(loginTokens.accessToken(), loginTokens.refreshToken()));
    }

    public void updateAccessTokens(Token dbAccessToken) {
        revokeAllUserAccessTokens(dbAccessToken.getUsername());
        tokenRepository.save(dbAccessToken);
    }

    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    public void revokeAllUserAccessTokens(String username){
        List<Token> allValidTokensByUser = tokenRepository.findAllValidTokensByUser(username);
        List<Token> validAccessTokens = allValidTokensByUser.stream()
                .filter(token -> token.getTokenType().equals(TokenType.ACCESS))
                .toList();
        revokeValidTokens(validAccessTokens);
    }

    public void revokeAllUserTokens(String username){
        List<Token> allValidTokensByUser = tokenRepository.findAllValidTokensByUser(username);
        revokeValidTokens(allValidTokensByUser);
    }

    private void revokeValidTokens(List<Token> validTokens){
        if(validTokens.isEmpty())
            return;
        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

}
