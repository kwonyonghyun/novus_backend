//package com.example.Novus.service;
//
//import com.example.Novus.config.jwt.TokenProvider;
//import com.example.Novus.domain.User;
//import com.example.Novus.dto.TokenResponse;
//import com.example.Novus.exception.InvalidTokenException;
//import com.example.Novus.exception.UserNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TokenServiceTest {
//
//    @Mock
//    private TokenProvider tokenProvider;
//
//    @Mock
//    private RefreshTokenService refreshTokenService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private TokenService tokenService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void refreshToken_validToken() {
//        // Given
//        String refreshToken = "valid_refresh_token";
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//
//        when(tokenProvider.validateToken(refreshToken)).thenReturn(true);
//        when(tokenProvider.getUserIdFromToken(refreshToken)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(tokenProvider.generateAccessToken(user)).thenReturn("new_access_token");
//        when(tokenProvider.generateRefreshToken(user)).thenReturn("new_refresh_token");
//
//        // When
//        TokenResponse response = tokenService.refreshToken(refreshToken);
//
//        // Then
//        assertEquals("new_access_token", response.getAccessToken());
//        assertEquals("new_refresh_token", response.getRefreshToken());
//        verify(refreshTokenService).saveRefreshToken(userId, "new_refresh_token");
//    }
//
//    @Test
//    void refreshToken_invalidToken() {
//        // Given
//        String refreshToken = "invalid_refresh_token";
//        when(tokenProvider.validateToken(refreshToken)).thenReturn(false);
//
//        // When & Then
//        assertThrows(InvalidTokenException.class, () -> tokenService.refreshToken(refreshToken));
//    }
//
//    @Test
//    void refreshToken_userNotFound() {
//        // Given
//        String refreshToken = "valid_refresh_token";
//        Long userId = 1L;
//
//        when(tokenProvider.validateToken(refreshToken)).thenReturn(true);
//        when(tokenProvider.getUserIdFromToken(refreshToken)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(UserNotFoundException.class, () -> tokenService.refreshToken(refreshToken));
//    }
//
//    @Test
//    void logout_validToken() {
//        // Given
//        String refreshToken = "valid_refresh_token";
//        Long userId = 1L;
//
//        when(tokenProvider.validateToken(refreshToken)).thenReturn(true);
//        when(tokenProvider.getUserIdFromToken(refreshToken)).thenReturn(userId);
//
//        // When
//        tokenService.logout(refreshToken);
//
//        // Then
//        verify(refreshTokenService).deleteByUserId(userId);
//    }
//
//    @Test
//    void logout_invalidToken() {
//        // Given
//        String refreshToken = "invalid_refresh_token";
//        when(tokenProvider.validateToken(refreshToken)).thenReturn(false);
//
//        // When & Then
//        assertThrows(InvalidTokenException.class, () -> tokenService.logout(refreshToken));
//    }
//}