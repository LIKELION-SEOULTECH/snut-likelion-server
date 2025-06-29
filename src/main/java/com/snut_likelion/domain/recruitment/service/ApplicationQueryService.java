package com.snut_likelion.domain.recruitment.service;

import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
<<<<<<< HEAD
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

=======
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

>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
@Service
@RequiredArgsConstructor
public class ApplicationQueryService {

<<<<<<< HEAD
    @Value("${snut.likelion.current-generation}")
    private int currentGeneration;

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public ApplicationDetailsResponse getMyApplication(Long userId) {
        Application application = applicationRepository.findMyApplication(userId, currentGeneration)
=======
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
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        return ApplicationDetailsResponse.from(application);
    }
}
