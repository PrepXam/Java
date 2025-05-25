package com.java.ne_starter.dtos.auth;



import com.java.ne_starter.models.User;
import lombok.Data;

@Data
public class AuthResponse {
    private User user;
    private TokensResponse tokens;

    public AuthResponse(String accessToken, User user) {
        this.tokens = new TokensResponse(accessToken);
        this.user = user;
    }
}

