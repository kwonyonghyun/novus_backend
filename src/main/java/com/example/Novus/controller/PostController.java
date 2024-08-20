package com.example.Novus.controller;

import com.example.Novus.dto.PostCreateRequest;
import com.example.Novus.dto.PostResponse;
import com.example.Novus.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("content") PostCreateRequest postCreateRequest,
            @RequestPart("media") List<MultipartFile> mediaFiles) throws IOException {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(postService.createPost(userId, postCreateRequest, mediaFiles));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(postService.getUserPosts(userId, pageable));
    }
}