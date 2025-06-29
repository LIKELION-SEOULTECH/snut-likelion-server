package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.Application;
<<<<<<< HEAD
import com.snut_likelion.domain.recruitment.entity.ApplicationStatus;
=======
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query(
            "SELECT a FROM Application a " +
                    "JOIN FETCH a.user u " +
                    "LEFT JOIN FETCH a.answers ans " +
                    "LEFT JOIN FETCH ans.question q " +
<<<<<<< HEAD
                    "WHERE a.user.id = :userId " +
                    "AND a.recruitment.generation = :currentGeneration"
    )
    Optional<Application> findMyApplication(
            @Param("userId") Long userId,
            @Param("currentGeneration") int currentGeneration
    );
=======
                    "WHERE u.id = :userId"
    )
    Optional<Application> findMyApplication(@Param("userId") Long userId);
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

    @Query(
            "SELECT a FROM Application a " +
                    "JOIN FETCH a.user u " +
                    "LEFT JOIN FETCH a.answers ans " +
                    "LEFT JOIN FETCH ans.question q " +
<<<<<<< HEAD
                    "WHERE a.id = :id " +
                    "AND a.recruitment.generation = :currentGeneration"
    )
    Optional<Application> findWithDetailsById(
            @Param("id") Long id,
            @Param("currentGeneration") int currentGeneration
    );

    @Query("SELECT a FROM Application a join fetch User u ON a.user.id = u.id WHERE a.id = :id")
    Optional<Application> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT a FROM Application a join fetch User u ON a.user.id = u.id " +
            "WHERE a.recruitment.generation = :currentGeneration " +
            "AND a.status = :status")
    List<Application> findAllByStatus(
            @Param("status") ApplicationStatus status,
            @Param("currentGeneration") int currentGeneration
    );
=======
                    "WHERE a.id = :id"
    )
    Optional<Application> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT a FROM Application a join fetch User u ON a.user.id = u.id WHERE a.id = :id")
    Optional<Application> findByIdWithUser(Long id);
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
