//package com.example.Novus.service;
//
//import com.example.Novus.config.jwt.TokenProvider;
//import com.example.Novus.domain.OAuthProvider;
//import com.example.Novus.domain.User;
//import com.example.Novus.dto.OAuth2LoginRequest;
//import com.example.Novus.dto.TokenResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class OAuth2ServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private TokenProvider tokenProvider;
//
//    @Mock
//    private RefreshTokenService refreshTokenService;
//
//    @InjectMocks
//    private OAuth2Service oAuth2Service;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void loginOrSignUp_existingUser() {
//        // Given
//        OAuth2LoginRequest request = new OAuth2LoginRequest("test@example.com", "Test User", "Test", "User", "http://example.com/profile.jpg");
//        User existingUser = new User("test@example.com", "Test User", "Test", "User", "http://example.com/profile.jpg", OAuthProvider.GOOGLE);
//        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
//        when(tokenProvider.generateAccessToken(existingUser)).thenReturn("access_token");
//        when(tokenProvider.generateRefreshToken(existingUser)).thenReturn("refresh_token");
//
//        // When
//        TokenResponse response = oAuth2Service.loginOrSignUp(request);
//
//        // Then
//        assertEquals("access_token", response.getAccessToken());
//        assertEquals("refresh_token", response.getRefreshToken());
//        verify(refreshTokenService).saveRefreshToken(existingUser.getId(), "refresh_token");
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void loginOrSignUp_newUser() {
//        // Given
//        OAuth2LoginRequest request = new OAuth2LoginRequest("test@example.com", "Test User", "Test", "User", "http://example.com/profile.jpg");
//        User newUser = new User("test@example.com", "Test User", "Test", "User", "http://example.com/profile.jpg", OAuthProvider.GOOGLE);
//        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(newUser);
//        when(tokenProvider.generateAccessToken(newUser)).thenReturn("access_token");
//        when(tokenProvider.generateRefreshToken(newUser)).thenReturn("refresh_token");
//
//        // When
//        TokenResponse response = oAuth2Service.loginOrSignUp(request);
//
//        // Then
//        assertEquals("access_token", response.getAccessToken());
//        assertEquals("refresh_token", response.getRefreshToken());
//        verify(refreshTokenService).saveRefreshToken(newUser.getId(), "refresh_token");
//        verify(userRepository).save(any(User.class));
//    }
//}