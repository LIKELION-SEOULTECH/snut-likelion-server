package com.snut_likeliion.domain.user.repository;

import com.snut_likeliion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmailAndUsername(String email, String username);

    boolean existsByEmailOrUsername(String email, String username);
}
