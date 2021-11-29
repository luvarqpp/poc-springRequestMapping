package sk.qpp.poc.security.controller.payload;

import lombok.Data;

@Data
public class AuthResponse {
    private final String accessToken;
    private final String tokenType = "Bearer";
}
