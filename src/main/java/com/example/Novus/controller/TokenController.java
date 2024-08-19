package com.example.Novus.controller;

import com.example.Novus.dto.TokenRefreshRequest;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/api/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        TokenResponse tokenResponse = tokenService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(@RequestBody TokenRefreshRequest request) {
        tokenService.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}