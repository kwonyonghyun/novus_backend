package com.example.Novus.controller;

import com.example.Novus.dto.UserDTO;
import com.example.Novus.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserDTO>> getSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptions(userId));
    }

    @PostMapping("/{subscriberId}/subscribe/{subscribedId}")
    public ResponseEntity<Void> subscribe(@PathVariable Long subscriberId, @PathVariable Long subscribedId) {
        subscriptionService.subscribe(subscriberId, subscribedId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{subscriberId}/unsubscribe/{subscribedId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long subscriberId, @PathVariable Long subscribedId) {
        subscriptionService.unsubscribe(subscriberId, subscribedId);
        return ResponseEntity.ok().build();
    }
}