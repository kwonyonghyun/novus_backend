package com.example.Novus.service;

import com.example.Novus.domain.Comment;
import com.example.Novus.domain.Post;
import com.example.Novus.domain.User;
import com.example.Novus.dto.CommentCreateRequest;
import com.example.Novus.dto.CommentResponse;
import com.example.Novus.exception.CommentNotFoundException;
import com.example.Novus.exception.PostNotFoundException;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.CommentRepository;
import com.example.Novus.repository.PostRepository;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Long userId, CommentCreateRequest request) {
        User user = userRepository.getById(userId);
        Post post = postRepository.getById(request.getPostId());

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(request.getContent());

        if (request.getParentId() != null) {
            Comment parentComment = commentRepository.getById(request.getParentId());
            comment.setParent(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);
        return convertToCommentResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsForPost(Long postId, Pageable pageable) {
        Post post = postRepository.getById(postId);
        Page<Comment> comments = commentRepository.findByPostAndParentIsNull(post, pageable);
        return comments.map(this::convertToCommentResponse);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                new ArrayList<>()
        );

        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            response.setReplies(comment.getReplies().stream()
                    .map(this::convertToCommentResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }
}