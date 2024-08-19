package com.example.Novus.controller;

import com.example.Novus.dto.TokenRefreshRequest;
import com.example.Novus.dto.TokenResponse;
import com.example.Novus.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TokenControllerTest {

    private static final String VALID_REFRESH_TOKEN = "valid_refresh_token";
    private static final String NEW_ACCESS_TOKEN = "new_access_token";
    private static final String NEW_REFRESH_TOKEN = "new_refresh_token";

    private MockMvc mockMvc;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenController tokenController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tokenController).build();
    }

    @Test
    void refreshToken_success() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken(VALID_REFRESH_TOKEN);
        TokenResponse tokenResponse = new TokenResponse(NEW_ACCESS_TOKEN, NEW_REFRESH_TOKEN);

        when(tokenService.refreshToken(anyString())).thenReturn(tokenResponse);

        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(NEW_ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(NEW_REFRESH_TOKEN));
    }
}