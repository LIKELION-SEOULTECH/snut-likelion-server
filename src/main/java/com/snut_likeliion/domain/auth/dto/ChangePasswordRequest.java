package com.snut_likeliion.domain.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {

    private String code;
    private String email;
    private String newPassword;
    private String newPasswordConfirm;

    @Builder
    public ChangePasswordRequest(String code, String email, String newPassword, String newPasswordConfirm) {
        this.code = code;
        this.email = email;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(newPasswordConfirm);
    }
}
