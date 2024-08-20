package com.example.Novus.service;

import com.example.Novus.domain.Media;
import com.example.Novus.domain.Post;
import com.example.Novus.domain.User;
import com.example.Novus.dto.PostCreateRequest;
import com.example.Novus.dto.PostResponse;
import com.example.Novus.exception.PostNotFoundException;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.PostRepository;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public PostResponse createPost(Long userId, PostCreateRequest request, List<MultipartFile> mediaFiles) throws IOException {
        User user = userRepository.getById(userId);
        Post post = new Post();
        post.setUser(user);
        post.setContent(request.getContent());

        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile file : mediaFiles) {
            String fileUrl = s3Service.uploadFile(file);
            Media media = new Media();
            media.setUrl(fileUrl);
            media.setType(file.getContentType().startsWith("image") ? Media.MediaType.IMAGE : Media.MediaType.VIDEO);
            media.setPost(post);
            mediaList.add(media);
        }
        post.setMedia(mediaList);

        Post savedPost = postRepository.save(post);
        return convertToPostResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        post.incrementViewCount();
        return convertToPostResponse(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findByUser(user, pageable);
        return posts.map(this::convertToPostResponse);
    }

    private PostResponse convertToPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getContent(),
                post.getMedia().stream().map(Media::getUrl).collect(Collectors.toList()),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}