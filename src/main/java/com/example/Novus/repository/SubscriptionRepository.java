package com.example.Novus.repository;

import com.example.Novus.domain.Subscription;
import com.example.Novus.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findBySubscriberAndSubscribedTo(User subscriber, User subscribedTo);

    Page<Subscription> findBySubscriber(User subscriber, Pageable pageable);

    boolean existsBySubscriberAndSubscribedTo(User subscriber, User subscribedTo);
}