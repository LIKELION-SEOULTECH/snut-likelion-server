package com.snut_likeliion.domain.auth.dto;

import com.snut_likeliion.domain.user.entity.Role;
import com.snut_likeliion.domain.user.entity.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterReq {

    @Email(message = "이메일 형식에 맞추어 입력해주세요.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    @NotEmpty(message = "유저명을 입력해주세요.")
    @Size(min = 2, max = 10, message = "유저명은 2 ~ 10 글자입니다.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 6, max = 20, message = "비밀번호는 6 ~ 20 글자입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인값을 입력해주세요.")
    @Size(min = 6, max = 20, message = "비밀번호 확인값은 6 ~ 20 글자입니다.")
    private String confirmPassword;

    @NotEmpty(message = "연락처를 입력해주세요.")
    @Size(min = 11, max = 11, message = "연락처 길이가 올바르지 않습니다.")
    private String phoneNumber;

    private Boolean isEmailVerified;

    @Builder
    public RegisterReq(String email, String username, String password, String confirmPassword, String phoneNumber, Boolean isEmailVerified) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
        this.isEmailVerified = isEmailVerified;
    }

    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }

    @AssertTrue(message = "이메일 인증이 필요합니다.")
    public boolean isEmailVerified() {
        return this.isEmailVerified != null && this.isEmailVerified;
    }

    public User toEntity(Role role, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .username(this.username)
                .password(passwordEncoder.encode(this.password))
                .role(role)
                .build();
    }
}