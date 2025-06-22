package com.snut_likelion.domain.recruitment.dto.request;

import com.snut_likelion.domain.recruitment.entity.Recruitment;
import com.snut_likelion.domain.recruitment.entity.RecruitmentType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateRecruitmentRequest {

    private int generation;
    private String recruitmentType;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;

    @Builder
    public CreateRecruitmentRequest(int generation, String recruitmentType, LocalDateTime openDate, LocalDateTime closeDate) {
        this.generation = generation;
        this.recruitmentType = recruitmentType;
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    public Recruitment toEntity() {
        return Recruitment.builder()
                .generation(generation)
                .recruitmentType(RecruitmentType.valueOf(recruitmentType))
                .openDate(openDate)
                .closeDate(closeDate)
                .build();
    }
}
