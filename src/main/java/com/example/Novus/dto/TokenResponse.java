package com.example.Novus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}