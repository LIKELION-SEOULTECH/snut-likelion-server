package com.snut_likelion.domain.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordRequest {

    @NotEmpty(message = "기존 비밀번호를 입력해주세요.")
    private String oldPassword;

    @NotEmpty(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;

    @NotEmpty(message = "새 비밀번호 확인을 입력해주세요.")
    private String confirmPassword;

    @Builder
    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    @AssertTrue(message = "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
