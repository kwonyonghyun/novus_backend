package com.example.Novus.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}