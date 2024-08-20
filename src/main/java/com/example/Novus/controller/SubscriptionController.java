package com.example.Novus.controller;

import com.example.Novus.dto.SubscriptionResponse;
import com.example.Novus.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{subscribedToId}")
    public ResponseEntity<Void> subscribe(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long subscribedToId) {
        Long subscriberId = Long.parseLong(userDetails.getUsername());
        subscriptionService.subscribe(subscriberId, subscribedToId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{subscribedToId}")
    public ResponseEntity<Void> unsubscribe(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long subscribedToId) {
        Long subscriberId = Long.parseLong(userDetails.getUsername());
        subscriptionService.unsubscribe(subscriberId, subscribedToId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<SubscriptionResponse>> getSubscriptions(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(subscriptionService.getSubscriptions(userId, pageable));
    }
}