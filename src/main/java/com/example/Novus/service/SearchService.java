package com.example.Novus.service;

import com.example.Novus.domain.User;
import com.example.Novus.dto.UserSearchResponse;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserSearchResponse> searchUsers(String query) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(query);
        return users.stream()
                .map(this::convertToUserSearchResponse)
                .collect(Collectors.toList());
    }

    private UserSearchResponse convertToUserSearchResponse(User user) {
        return new UserSearchResponse(user.getId(), user.getEmail(), user.getName(), user.getProfilePictureUrl());
    }
}