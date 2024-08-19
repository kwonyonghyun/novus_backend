package com.example.Novus.domain;

import com.example.Novus.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "oauth_provider")
    @Enumerated(value = EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions = new ArrayList<>();

    @Builder
    public User(String email, String name, String familyName, String givenName, String profilePictureUrl, OAuthProvider oAuthProvider) {
        this.email = email;
        this.name = name;
        this.familyName = familyName;
        this.givenName = givenName;
        this.profilePictureUrl = profilePictureUrl;
        this.oAuthProvider = oAuthProvider;
    }

    public User updateProfilePicture(String newProfilePictureUrl) {
        this.profilePictureUrl = newProfilePictureUrl;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null; // OAuth2 사용자는 비밀번호가 없습니다.
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setSubscriber(this);
    }

    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
        subscription.setSubscriber(null);
    }

    public UserDTO toDTO() {
        return new UserDTO(this.id, this.email, this.name, this.profilePictureUrl);
    }
}