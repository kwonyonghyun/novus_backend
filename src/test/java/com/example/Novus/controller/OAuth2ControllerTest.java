package com.example.Novus.controller;

import com.example.Novus.dto.OAuth2LoginRequest;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.service.OAuth2Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OAuth2ControllerTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_NAME = "Test User";
    private static final String TEST_GIVEN_NAME = "Test";
    private static final String TEST_FAMILY_NAME = "User";
    private static final String TEST_PROFILE_URL = "http://example.com/profile.jpg";
    private static final String TEST_ACCESS_TOKEN = "access_token";
    private static final String TEST_REFRESH_TOKEN = "refresh_token";
    private static final Long TEST_USER_ID = 1L;

    private MockMvc mockMvc;

    @Mock
    private OAuth2Service oauth2Service;

    @InjectMocks
    private OAuth2Controller oauth2Controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(oauth2Controller).build();
    }

    @Test
    @DisplayName("OAuth2 로그인 성공")
    void oauth2Login_success() throws Exception {
        OAuth2LoginRequest request = new OAuth2LoginRequest(TEST_EMAIL, TEST_NAME, TEST_GIVEN_NAME, TEST_FAMILY_NAME, TEST_PROFILE_URL);
        TokenResponse tokenResponse = new TokenResponse(TEST_USER_ID, TEST_ACCESS_TOKEN, TEST_REFRESH_TOKEN);

        when(oauth2Service.loginOrSignUp(any(OAuth2LoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/oauth2/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID))
                .andExpect(jsonPath("$.accessToken").value(TEST_ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(TEST_REFRESH_TOKEN));
    }
}