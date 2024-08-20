package com.example.Novus.service;

import com.example.Novus.config.jwt.TokenProvider;
import com.example.Novus.domain.OAuthProvider;
import com.example.Novus.domain.User;
import com.example.Novus.dto.OAuth2LoginRequest;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenResponse loginOrSignUp(OAuth2LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> createUser(request));

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        return new TokenResponse(user.getId(), accessToken, refreshToken);
    }

    private User createUser(OAuth2LoginRequest request) {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setGivenName(request.getGivenName());
        newUser.setFamilyName(request.getFamilyName());
        newUser.setProfilePictureUrl(request.getProfilePictureUrl());
        newUser.setOAuthProvider(OAuthProvider.GOOGLE);
        return userRepository.save(newUser);
    }
}