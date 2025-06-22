package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query(
            "SELECT a FROM Application a " +
                    "JOIN FETCH a.user u " +
                    "LEFT JOIN FETCH a.answers ans " +
                    "LEFT JOIN FETCH ans.question q " +
                    "WHERE u.id = :userId"
    )
    Optional<Application> findMyApplication(@Param("userId") Long userId);

    @Query(
            "SELECT a FROM Application a " +
                    "JOIN FETCH a.user u " +
                    "LEFT JOIN FETCH a.answers ans " +
                    "LEFT JOIN FETCH ans.question q " +
                    "WHERE a.id = :id"
    )
    Optional<Application> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT a FROM Application a join fetch User u ON a.user.id = u.id WHERE a.id = :id")
    Optional<Application> findByIdWithUser(Long id);
}
