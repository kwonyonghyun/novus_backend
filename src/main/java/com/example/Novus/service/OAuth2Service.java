package com.example.Novus.service;

import com.example.Novus.config.jwt.TokenProvider;
import com.example.Novus.domain.OAuthProvider;
import com.example.Novus.domain.User;
import com.example.Novus.dto.OAuth2LoginRequest;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.exception.UserNotFoundException;
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

        return new TokenResponse(user.getId(),accessToken, refreshToken);
    }

    private User createUser(OAuth2LoginRequest request) {
        User newUser = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .givenName(request.getGivenName())
                .familyName(request.getFamilyName())
                .profilePictureUrl(request.getProfilePictureUrl())
                .oAuthProvider(OAuthProvider.GOOGLE)
                .build();
        return userRepository.save(newUser);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}