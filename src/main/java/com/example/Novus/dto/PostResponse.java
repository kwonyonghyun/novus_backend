package com.example.Novus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private Long userId;
    private String content;
    private List<String> mediaUrls;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
