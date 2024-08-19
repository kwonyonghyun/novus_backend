package com.example.Novus.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribed_id")
    private User subscribed;

    @Column(name = "subscribed_at")
    private LocalDateTime subscribedAt;

    public Subscription(User subscriber, User subscribed) {
        this.subscriber = subscriber;
        this.subscribed = subscribed;
        this.subscribedAt = LocalDateTime.now();
    }
}