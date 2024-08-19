package com.example.Novus.service;

import com.example.Novus.config.jwt.TokenProvider;
import com.example.Novus.domain.RefreshToken;
import com.example.Novus.domain.User;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.exception.InvalidTokenException;
import com.example.Novus.exception.RefreshTokenNotFoundException;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private static final String VALID_REFRESH_TOKEN = "valid_refresh_token";
    private static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";
    private static final String NEW_ACCESS_TOKEN = "new_access_token";
    private static final String NEW_REFRESH_TOKEN = "new_refresh_token";
    private static final Long USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_NAME = "Test User";
    private static final String TEST_FAMILY_NAME = "User";
    private static final String TEST_GIVEN_NAME = "Test";
    private static final String TEST_PROFILE_URL = "http://example.com/profile.jpg";

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유효한 리프레시 토큰으로 새 토큰 발급")
    void refreshToken_validToken() {
        RefreshToken refreshToken = new RefreshToken(1L, VALID_REFRESH_TOKEN, USER_ID);
        User user = new User(TEST_EMAIL, TEST_NAME, TEST_FAMILY_NAME, TEST_GIVEN_NAME, TEST_PROFILE_URL, null);

        when(tokenProvider.validateToken(VALID_REFRESH_TOKEN)).thenReturn(true);
        when(refreshTokenService.findByToken(VALID_REFRESH_TOKEN)).thenReturn(refreshToken);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(tokenProvider.generateAccessToken(user)).thenReturn(NEW_ACCESS_TOKEN);
        when(tokenProvider.generateRefreshToken(user)).thenReturn(NEW_REFRESH_TOKEN);

        TokenResponse response = tokenService.refreshToken(VALID_REFRESH_TOKEN);

        assertNotNull(response);
        assertEquals(NEW_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(NEW_REFRESH_TOKEN, response.getRefreshToken());
    }

    @Test
    @DisplayName("유효하지 않은 리프레시 토큰으로 예외 발생")
    void refreshToken_invalidToken() {
        when(tokenProvider.validateToken(INVALID_REFRESH_TOKEN)).thenReturn(false);

        assertThrows(RefreshTokenNotFoundException.class, () -> tokenService.refreshToken(INVALID_REFRESH_TOKEN));
    }

    @Test
    @DisplayName("존재하지 않는 리프레시 토큰으로 예외 발생")
    void refreshToken_tokenNotFound() {
        when(tokenProvider.validateToken(VALID_REFRESH_TOKEN)).thenReturn(true);
        when(refreshTokenService.findByToken(VALID_REFRESH_TOKEN)).thenThrow(new RefreshTokenNotFoundException("Token not found"));

        assertThrows(RefreshTokenNotFoundException.class, () -> tokenService.refreshToken(VALID_REFRESH_TOKEN));
    }

    @Test
    @DisplayName("리프레시 토큰에 해당하는 사용자가 없을 때 예외 발생")
    void refreshToken_userNotFound() {
        RefreshToken refreshToken = new RefreshToken(1L, VALID_REFRESH_TOKEN, USER_ID);

        when(tokenProvider.validateToken(VALID_REFRESH_TOKEN)).thenReturn(true);
        when(refreshTokenService.findByToken(VALID_REFRESH_TOKEN)).thenReturn(refreshToken);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> tokenService.refreshToken(VALID_REFRESH_TOKEN));
    }
}