package com.example.Novus.controller;

import com.example.Novus.dto.OAuth2LoginRequest;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/api/oauth2/login")
    public ResponseEntity<TokenResponse> oauth2Login(@RequestBody OAuth2LoginRequest request) {
        TokenResponse tokenResponse = oAuth2Service.loginOrSignUp(request);
        return ResponseEntity.ok(tokenResponse);
    }
}