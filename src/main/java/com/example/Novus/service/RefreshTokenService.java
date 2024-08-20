package com.example.Novus.service;

import com.example.Novus.domain.RefreshToken;
import com.example.Novus.domain.User;
import com.example.Novus.exception.RefreshTokenNotFoundException;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.RefreshTokenRepository;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveRefreshToken(Long userId, String token) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        refreshTokenRepository.findByUser(foundUser)
                .ifPresentOrElse(
                        refreshToken -> refreshToken.updateToken(token),
                        () -> refreshTokenRepository.save(RefreshToken.builder()
                                .user(foundUser)
                                .token(token)
                                .build())
                );
    }

    @Transactional(readOnly = true)
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        refreshTokenRepository.deleteByUser(foundUser);
    }
}