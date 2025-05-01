package com.snut_likeliion.global.auth.model;

import com.snut_likeliion.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;

    @Builder
    public UserInfo(Long id, String username, String email, String password, boolean isUnivVerified, String role, String provider) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static UserInfo from(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
    }
}