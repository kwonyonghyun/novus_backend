package com.example.Novus.dto;

import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateRequest {
    private String displayName;
    private String bio;
    private BigDecimal price;
}