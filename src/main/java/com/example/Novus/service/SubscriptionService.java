package com.example.Novus.service;

import com.example.Novus.domain.Subscription;
import com.example.Novus.domain.User;
import com.example.Novus.dto.SubscriptionResponse;
import com.example.Novus.exception.UserNotFoundException;
import com.example.Novus.repository.SubscriptionRepository;
import com.example.Novus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void subscribe(Long subscriberId, Long subscribedToId) {
        User subscriber = userRepository.getById(subscriberId);
        User subscribedTo = userRepository.getById(subscribedToId);

        if (!subscriptionRepository.existsBySubscriberAndSubscribedTo(subscriber, subscribedTo)) {
            Subscription subscription = new Subscription();
            subscription.setSubscriber(subscriber);
            subscription.setSubscribedTo(subscribedTo);
            subscriptionRepository.save(subscription);
        }
    }

    @Transactional
    public void unsubscribe(Long subscriberId, Long subscribedToId) {
        User subscriber = userRepository.getById(subscriberId);
        User subscribedTo = userRepository.getById(subscribedToId);

        subscriptionRepository.findBySubscriberAndSubscribedTo(subscriber, subscribedTo)
                .ifPresent(subscriptionRepository::delete);
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionResponse> getSubscriptions(Long userId, Pageable pageable) {
        User user = userRepository.getById(userId);
        Page<Subscription> subscriptions = subscriptionRepository.findBySubscriber(user, pageable);
        return subscriptions.map(this::convertToSubscriptionResponse);
    }

    private SubscriptionResponse convertToSubscriptionResponse(Subscription subscription) {
        User subscribedTo = subscription.getSubscribedTo();
        return new SubscriptionResponse(
                subscribedTo.getId(),
                subscribedTo.getEmail(),
                subscribedTo.getName(),
                subscribedTo.getDisplayName(),
                subscribedTo.getProfilePictureUrl()
        );
    }
}