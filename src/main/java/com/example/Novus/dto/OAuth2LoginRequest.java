package com.example.Novus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2LoginRequest {
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String profilePictureUrl;
}