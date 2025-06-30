package com.snut_likelion.admin.recruitment.service;

import com.snut_likelion.admin.recruitment.dto.request.ApplicationListStatus;
import com.snut_likelion.admin.recruitment.dto.request.ChangeApplicationStatusParameter;
import com.snut_likelion.admin.recruitment.dto.request.ChangeApplicationStatusRequest;
import com.snut_likelion.admin.recruitment.dto.response.ApplicationPageResponse;
import com.snut_likelion.admin.recruitment.infra.ApplicationQueryRepository;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.entity.ApplicationStatus;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.Role;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.snut_likelion.domain.recruitment.entity.ApplicationStatus.*;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {

    private final int PAGE_SIZE = 8;

    @Value("${snut.likelion.current-generation}")
    private int currentGeneration;

    private final ApplicationQueryRepository applicationQueryRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationService notificationService;

    public ApplicationPageResponse getApplicationsByRecruitmentId(Long recId, Part part, int page, ApplicationListStatus status) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<ApplicationPageResponse.ApplicationListResponse> result =
                applicationQueryRepository.getApplicationList(recId, part, status, pageRequest);
        return ApplicationPageResponse.from(result);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailsResponse getApplicationDetails(Long appId) {
        Application application = applicationRepository.findWithDetailsById(appId, currentGeneration)
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        return ApplicationDetailsResponse.from(application);
    }

    @Transactional
    public void updateApplicationStatus(ChangeApplicationStatusParameter status, ChangeApplicationStatusRequest req) {
        if (status == ChangeApplicationStatusParameter.PAPER_PASS) {
            List<Application> apps = applicationRepository.findAllByStatus(SUBMITTED, currentGeneration);

            apps.forEach(app -> {
                User user = app.getUser();

                if (req.getIds().contains(app.getId())) {
                    this.doProcess(app, PAPER_PASS, user);
                } else {
                    this.doProcess(app, FAILED, user);
                }
            });
        } else if (status == ChangeApplicationStatusParameter.FINAL_PASS) {
            List<Application> apps = applicationRepository.findAllByStatus(PAPER_PASS, currentGeneration);

            apps.forEach(app -> {
                User user = app.getUser();
                if (req.getIds().contains(app.getId())) {
                    this.generateLionInfo(app, user);
                    this.doProcess(app, FINAL_PASS, user);
                } else {
                    this.doProcess(app, FAILED, user);
                }
            });
        }
    }

    private void generateLionInfo(Application app, User user) {
        Role role = app.getDepartmentType() == null ? Role.ROLE_USER : Role.ROLE_MANAGER;
        user.generateCurrentLionInfo(currentGeneration, app.getPart(), role, app.getDepartmentType());
    }

    private void doProcess(Application app, ApplicationStatus status, User user) {
        app.setStatus(status);
        notificationService.sendNotification(user, status, app);
    }
}
