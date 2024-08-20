package com.example.Novus.controller;

import com.example.Novus.dto.UserProfileResponse;
import com.example.Novus.dto.UserProfileUpdateRequest;
import com.example.Novus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("user") UserProfileUpdateRequest userProfileUpdateRequest,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(userService.updateUserProfile(userId, userProfileUpdateRequest, profilePicture));
    }
}