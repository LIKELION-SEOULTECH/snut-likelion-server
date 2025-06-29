package com.snut_likelion.global.auth.checker;

import com.snut_likelion.domain.project.infra.ProjectParticipationRepository;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.domain.user.entity.LionInfo;
import com.snut_likelion.domain.user.repository.LionInfoRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("authChecker")
@RequiredArgsConstructor
public class AuthChecker {

    private final LionInfoRepository lionInfoRepository;
    private final ProjectParticipationRepository projectParticipationRepository;
    private final ApplicationRepository applicationRepository;

    private boolean hasManagerAuthority(UserInfo user) {
        return List.of("ROLE_ADMIN", "ROLE_MANAGER").contains(user.getRole());
    }

    public boolean isMe(UserInfo userInfo, Long memberId) {
        return userInfo.getId().equals(memberId) || this.hasManagerAuthority(userInfo);
    }

    public boolean isMyProject(UserInfo userInfo, Long projectId) {
        List<LionInfo> lionInfos = lionInfoRepository.findByUser_Id(userInfo.getId());
        List<Long> lionInfoIds = lionInfos.stream().map(LionInfo::getId).toList();
        boolean isMyProject = projectParticipationRepository.existsByProject_IdAndLionInfo_Ids(projectId, lionInfoIds);

        return isMyProject || this.hasManagerAuthority(userInfo);
    }

    public boolean isMyApplication(UserInfo userInfo, Long appId) {
        Application application = applicationRepository.findByIdWithUser(appId)
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        Long userId = application.getUser().getId();
        return userId.equals(userInfo.getId()) || this.hasManagerAuthority(userInfo);
    }
}
