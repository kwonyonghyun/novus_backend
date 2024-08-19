package com.example.Novus.service;

import com.example.Novus.domain.Subscription;
import com.example.Novus.domain.User;
import com.example.Novus.dto.UserDTO;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.SubscriptionRepository;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public void subscribe(Long subscriberId, Long subscribedId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new UserNotFoundException("Subscriber not found"));
        User subscribed = userRepository.findById(subscribedId)
                .orElseThrow(() -> new UserNotFoundException("Subscribed user not found"));

        if (subscriptionRepository.findBySubscriberAndSubscribed(subscriber, subscribed).isEmpty()) {
            Subscription subscription = new Subscription(subscriber, subscribed);
            subscriber.addSubscription(subscription);
            subscriptionRepository.save(subscription);
        }
    }

    @Transactional
    public void unsubscribe(Long subscriberId, Long subscribedId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new UserNotFoundException("Subscriber not found"));
        User subscribed = userRepository.findById(subscribedId)
                .orElseThrow(() -> new UserNotFoundException("Subscribed user not found"));

        subscriptionRepository.findBySubscriberAndSubscribed(subscriber, subscribed)
                .ifPresent(subscription -> {
                    subscriber.removeSubscription(subscription);
                    subscriptionRepository.delete(subscription);
                });
    }

    public List<UserDTO> getSubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getSubscriptions().stream()
                .map(subscription -> subscription.getSubscribed().toDTO())
                .collect(Collectors.toList());
    }
}