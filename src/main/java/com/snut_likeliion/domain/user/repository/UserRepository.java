package com.snut_likeliion.domain.user.repository;

import com.snut_likeliion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u join fetch LionInfo li on u.id = li.user.id " +
            "where u.email = :email")
    Optional<User> findWithLionInfoByEmail(String email);

    boolean existsByEmailAndUsername(String email, String username);

    boolean existsByEmailOrUsername(String email, String username);

    @Query("select u from User u join fetch u.lionInfos " +
            "where u.id = :userId")
    Optional<User> findWithLionUserById(Long userId);

    @Query("select u from User u " +
            "left join fetch u.portfolioLinks " +
            "where u.id = :userId")
    Optional<User> findUserDetailsByUserId(@Param("userId") Long userId);

}
