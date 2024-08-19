package com.example.Novus.service;

import com.example.Novus.config.jwt.TokenProvider;
import com.example.Novus.domain.RefreshToken;
import com.example.Novus.domain.User;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.exception.RefreshTokenNotFoundException;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse refreshToken(String refreshTokenString) {
        if (!tokenProvider.validateToken(refreshTokenString)) {
            throw new RefreshTokenNotFoundException("Invalid refresh token");
        }

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenString);
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newAccessToken = tokenProvider.generateAccessToken(user);
        String newRefreshToken = tokenProvider.generateRefreshToken(user);

        refreshTokenService.saveRefreshToken(user.getId(), newRefreshToken);

        return new TokenResponse(user.getId(),newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken);
        refreshTokenService.deleteByUserId(token.getUserId());
    }
}