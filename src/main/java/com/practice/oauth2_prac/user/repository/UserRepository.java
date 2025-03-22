package com.practice.oauth2_prac.user.repository;

import com.practice.oauth2_prac.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialId(String socialId);
}