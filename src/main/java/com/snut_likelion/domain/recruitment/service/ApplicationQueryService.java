package com.snut_likelion.domain.recruitment.service;

import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationQueryService {

    @Value("${snut.likelion.current-generation}")
    private int currentGeneration;

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public ApplicationDetailsResponse getMyApplication(Long userId) {
        Application application = applicationRepository.findMyApplication(userId, currentGeneration)
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        return ApplicationDetailsResponse.from(application);
    }
}
