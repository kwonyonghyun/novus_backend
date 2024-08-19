package com.example.Novus.service;

import com.example.Novus.config.jwt.TokenProvider;
import com.example.Novus.domain.User;
import com.example.Novus.dto.OAuth2LoginRequest;
import com.example.Novus.dto.TokenResponse;
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

class OAuth2ServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_NAME = "Test User";
    private static final String TEST_GIVEN_NAME = "Test";
    private static final String TEST_FAMILY_NAME = "User";
    private static final String TEST_PROFILE_URL = "http://example.com/profile.jpg";
    private static final String TEST_ACCESS_TOKEN = "access_token";
    private static final String TEST_REFRESH_TOKEN = "refresh_token";
    private static final Long TEST_USER_ID = 1L;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private OAuth2Service oauth2Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("로그인 - 기존 사용자")
    void loginOrSignUp_existingUser() {
        OAuth2LoginRequest request = new OAuth2LoginRequest(TEST_EMAIL, TEST_NAME, TEST_GIVEN_NAME, TEST_FAMILY_NAME, TEST_PROFILE_URL);
        User existingUser = new User(TEST_EMAIL, TEST_NAME, TEST_FAMILY_NAME, TEST_GIVEN_NAME, TEST_PROFILE_URL, null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        when(tokenProvider.generateAccessToken(any(User.class))).thenReturn(TEST_ACCESS_TOKEN);
        when(tokenProvider.generateRefreshToken(any(User.class))).thenReturn(TEST_REFRESH_TOKEN);

        TokenResponse response = oauth2Service.loginOrSignUp(request);

        assertNotNull(response);
        assertEquals(TEST_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getRefreshToken());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("로그인 - 신규 사용자")
    void loginOrSignUp_newUser() {
        OAuth2LoginRequest request = new OAuth2LoginRequest(TEST_EMAIL, TEST_NAME, TEST_GIVEN_NAME, TEST_FAMILY_NAME, TEST_PROFILE_URL);
        User newUser = new User(TEST_EMAIL, TEST_NAME, TEST_FAMILY_NAME, TEST_GIVEN_NAME, TEST_PROFILE_URL, null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(tokenProvider.generateAccessToken(any(User.class))).thenReturn(TEST_ACCESS_TOKEN);
        when(tokenProvider.generateRefreshToken(any(User.class))).thenReturn(TEST_REFRESH_TOKEN);

        TokenResponse response = oauth2Service.loginOrSignUp(request);

        assertNotNull(response);
        assertEquals(TEST_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getRefreshToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 ID로 사용자 찾기 - 존재하는 사용자")
    void findUserById_userExists() {
        User user = new User(TEST_EMAIL, TEST_NAME, TEST_FAMILY_NAME, TEST_GIVEN_NAME, TEST_PROFILE_URL, null);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));

        User foundUser = oauth2Service.findUserById(TEST_USER_ID);

        assertNotNull(foundUser);
        assertEquals(TEST_EMAIL, foundUser.getEmail());
    }

    @Test
    @DisplayName("사용자 ID로 사용자 찾기 - 존재하지 않는 사용자")
    void findUserById_userNotFound() {
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> oauth2Service.findUserById(TEST_USER_ID));
    }
}