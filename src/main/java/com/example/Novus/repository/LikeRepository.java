package com.example.Novus.repository;

import com.example.Novus.domain.Like;
import com.example.Novus.domain.Post;
import com.example.Novus.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    boolean existsByUserAndPost(User user, Post post);

    Long countByPost(Post post);
}