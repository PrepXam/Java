package com.java.ne_starter.dtos.auth;

import lombok.Data;

@Data
public class TokensResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public TokensResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
