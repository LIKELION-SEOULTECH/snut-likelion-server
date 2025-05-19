package com.snut_likeliion.domain.user.entity;


import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username; // 실제 이름

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Part part; // 파트

    private String phoneNumber; // 연락처

    private List<Integer> generations; // 기수

    private String description; // 자기 소개

    @Builder
    public User(String email, String username, String password, Role role, Part part, String phoneNumber, List<Integer> generations, String description) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.part = part;
        this.phoneNumber = phoneNumber;
        this.generations = generations;
        this.description = description;
    }
}
