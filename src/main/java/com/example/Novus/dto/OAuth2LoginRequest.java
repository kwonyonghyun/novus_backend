package com.example.Novus.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2LoginRequest {
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String profilePictureUrl;
}