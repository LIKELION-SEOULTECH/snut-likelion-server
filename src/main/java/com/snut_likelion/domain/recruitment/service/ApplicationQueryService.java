package com.snut_likelion.domain.recruitment.service;

import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationResponse;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationQueryRepository;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationQueryService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationQueryRepository applicationQueryRepository;

    public List<ApplicationResponse> getApplicationsByRecruitmentId(Long recId, Part part, int page) {
        return applicationQueryRepository.getApplicationsByRecruitmentId(recId, part, page);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailsResponse getMyApplication(Long userId) {
        Application application = applicationRepository.findMyApplication(userId)
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        return ApplicationDetailsResponse.from(application);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailsResponse getApplicationDetails(Long appId) {
        Application application = applicationRepository.findWithDetailsById(appId)
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        return ApplicationDetailsResponse.from(application);
    }
}
