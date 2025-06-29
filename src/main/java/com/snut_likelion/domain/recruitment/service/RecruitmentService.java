package com.snut_likelion.domain.recruitment.service;

<<<<<<< HEAD
import com.snut_likelion.admin.recruitment.dto.request.CreateRecruitmentRequest;
import com.snut_likelion.admin.recruitment.dto.request.UpdateRecruitmentRequest;
=======
import com.snut_likelion.domain.recruitment.dto.request.CreateRecruitmentRequest;
import com.snut_likelion.domain.recruitment.dto.request.UpdateRecruitmentRequest;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.recruitment.dto.response.RecruitmentResponse;
import com.snut_likelion.domain.recruitment.entity.Recruitment;
import com.snut_likelion.domain.recruitment.entity.RecruitmentType;
import com.snut_likelion.domain.recruitment.exception.RecruitmentErrorCode;
import com.snut_likelion.domain.recruitment.infra.RecruitmentRepository;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    public RecruitmentResponse getRecentRecruitmentInfo(RecruitmentType recruitmentType) {
        Recruitment recruitment = recruitmentRepository.findRecentRecruitment(recruitmentType)
                .orElseThrow(() -> new NotFoundException(RecruitmentErrorCode.NOT_FOUND_RECRUITMENT));
        return RecruitmentResponse.from(recruitment);
    }

<<<<<<< HEAD
=======
    @Transactional
    public void createRecruitment(CreateRecruitmentRequest req) {
        Recruitment recruitment = req.toEntity();
        recruitmentRepository.save(recruitment);
    }

    @Transactional
    public void updateRecruitment(Long recId, UpdateRecruitmentRequest req) {
        Recruitment recruitment = recruitmentRepository.findById(recId)
                .orElseThrow(() -> new NotFoundException(RecruitmentErrorCode.NOT_FOUND_RECRUITMENT));
        recruitment.update(req.getGeneration(), req.getRecruitmentType(), req.getOpenDate(), req.getCloseDate());
    }

    @Transactional
    public void removeRecruitment(Long recId) {
        Recruitment recruitment = recruitmentRepository.findById(recId)
                .orElseThrow(() -> new NotFoundException(RecruitmentErrorCode.NOT_FOUND_RECRUITMENT));
        recruitmentRepository.delete(recruitment);
    }
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
