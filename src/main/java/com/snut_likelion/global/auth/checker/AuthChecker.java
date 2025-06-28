package com.snut_likelion.global.auth.checker;

import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectParticipation;
import com.snut_likelion.domain.project.exception.ProjectErrorCode;
import com.snut_likelion.domain.project.infra.ProjectParticipationRepository;
import com.snut_likelion.domain.project.infra.ProjectRepository;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.domain.user.entity.LionInfo;
import com.snut_likelion.domain.user.repository.LionInfoRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component("authChecker")
@RequiredArgsConstructor
public class AuthChecker {

    private final LionInfoRepository lionInfoRepository;
    private final ProjectParticipationRepository projectParticipationRepository;
    private final ApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;

    private boolean hasManagerAuthority(UserInfo user) {
        return List.of("ROLE_ADMIN", "ROLE_MANAGER").contains(user.getRole());
    }

    public boolean isMe(UserInfo userInfo, Long memberId) {
        return userInfo.getId().equals(memberId) || this.hasManagerAuthority(userInfo);
    }

    @Transactional(readOnly = true)
    public boolean isMyProject(UserInfo userInfo, Long projectId) {
        List<LionInfo> lionInfos = lionInfoRepository.findByUser_Id(userInfo.getId());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        List<ProjectParticipation> participations = project.getParticipations();

        if (participations.isEmpty()) return true;

        Optional<LionInfo> optionalLionInfo = lionInfos.stream()
                .filter(li -> li.getGeneration() == project.getGeneration())
                .findFirst();

        if (optionalLionInfo.isEmpty()) return false;

        LionInfo lionInfo = optionalLionInfo.get();

        return participations.stream()
                .anyMatch(projectParticipation -> projectParticipation.getLionInfo().equals(lionInfo));
    }

    public boolean isMyApplication(UserInfo userInfo, Long appId) {
        Application application = applicationRepository.findByIdWithUser(appId)
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        Long userId = application.getUser().getId();
        return userId.equals(userInfo.getId()) || this.hasManagerAuthority(userInfo);
    }
}
