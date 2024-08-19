package com.example.Novus.repository;

import com.example.Novus.domain.Subscription;
import com.example.Novus.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findBySubscriber(User subscriber);
    Optional<Subscription> findBySubscriberAndSubscribed(User subscriber, User subscribed);
}