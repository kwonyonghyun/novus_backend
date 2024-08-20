package com.example.Novus.service;

import com.example.Novus.domain.User;
import com.example.Novus.dto.UserProfileResponse;
import com.example.Novus.dto.UserProfileUpdateRequest;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToUserProfileResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UserProfileUpdateRequest request, MultipartFile profilePicture) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setDisplayName(request.getDisplayName());
        user.setBio(request.getBio());
        user.setPrice(request.getPrice());

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String profilePictureUrl = s3Service.uploadFile(profilePicture);
            user.setProfilePictureUrl(profilePictureUrl);
        }

        return convertToUserProfileResponse(userRepository.save(user));
    }

    private UserProfileResponse convertToUserProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getDisplayName(),
                user.getBio(),
                user.getProfilePictureUrl(),
                user.getPrice()
        );
    }
}
