package com.practice.oauth2_prac.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String socialId; // 소셜 로그인 ID

    @Builder
    public User(String email, String nickname, String socialId) {
        this.email = email;
        this.nickname = nickname;
        this.socialId = socialId;
    }
}
