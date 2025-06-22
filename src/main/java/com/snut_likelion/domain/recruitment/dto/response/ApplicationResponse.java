package com.snut_likelion.domain.recruitment.dto.response;

import com.snut_likelion.domain.recruitment.entity.ApplicationStatus;
import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.user.entity.Part;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationResponse {

    private Long id;
    private String username;
    private String part; // 지원 파트
    private String departmentType; // 부서 (운영진 지원일 경우)
    private String status;
    private LocalDateTime submittedAt; // 지원서 제출 시간

    @Builder
    public ApplicationResponse(Long id, String username, Part part, DepartmentType departmentType, ApplicationStatus status, LocalDateTime submittedAt) {
        this.id = id;
        this.username = username;
        this.part = part.name();
        this.departmentType = departmentType != null ? departmentType.name() : null;
        this.status = status.name();
        this.submittedAt = submittedAt;
    }
}
