package com.snut_likelion.admin.recruitment.dto.response;

import com.snut_likelion.domain.recruitment.entity.ApplicationStatus;
import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.user.entity.Part;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationPageResponse {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<ApplicationListResponse> content;

    @Builder
    public ApplicationPageResponse(int page, int size, long totalElements, int totalPages, List<ApplicationListResponse> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    public static ApplicationPageResponse from(Page<ApplicationPageResponse.ApplicationListResponse> page) {
        return ApplicationPageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent())
                .build();
    }

    @Getter
    @NoArgsConstructor
    public static class ApplicationListResponse {

        private Long id;
        private String username;
        private String part; // 지원 파트
        private String departmentType; // 부서 (운영진 지원일 경우)
        private String status;
        private LocalDateTime submittedAt; // 지원서 제출 시간

        @Builder
        public ApplicationListResponse(Long id, String username, Part part, DepartmentType departmentType, ApplicationStatus status, LocalDateTime submittedAt) {
            this.id = id;
            this.username = username;
            this.part = part.getDescription();
            this.departmentType = departmentType != null ? departmentType.getDescription() : null;
            this.status = status.getDescription();
            this.submittedAt = submittedAt;
        }
    }
}
