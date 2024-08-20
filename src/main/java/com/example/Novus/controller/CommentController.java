package com.example.Novus.controller;

import com.example.Novus.dto.CommentCreateRequest;
import com.example.Novus.dto.CommentResponse;
import com.example.Novus.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentCreateRequest commentCreateRequest) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(commentService.createComment(userId, commentCreateRequest));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<CommentResponse>> getCommentsForPost(
            @PathVariable Long postId,
            Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId, pageable));
    }
}