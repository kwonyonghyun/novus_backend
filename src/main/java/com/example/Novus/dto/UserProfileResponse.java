package com.example.Novus.dto;

import lombok.*;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private Long id;
    private String email;
    private String name;
    private String displayName;
    private String bio;
    private String profilePictureUrl;
    private BigDecimal price;
}
