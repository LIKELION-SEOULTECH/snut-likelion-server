package com.snut_likelion.global.auth.checker;

import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.domain.blog.repository.BlogPostRepository;
import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectParticipation;
import com.snut_likelion.domain.project.exception.ProjectErrorCode;
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
    private final ApplicationRepository applicationRepository;
    private final BlogPostRepository blogPostRepository;
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

        boolean isMyProject = participations.stream()
                .anyMatch(projectParticipation -> projectParticipation.getLionInfo().equals(lionInfo));

        return isMyProject || this.hasManagerAuthority(userInfo);
    }

    public boolean checkIsOfficialAndManager(Category category, UserInfo user) {
        return category != Category.OFFICIAL || this.hasManagerAuthority(user);
    }

    // 수정 & 삭제 권한 검사 (OFFICIAL 게시글은 관리자만 / UNOFFICIAL 게시글은 작성자 또는 관리자)
    public boolean checkCanModify(Long postId, UserInfo user) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        boolean isManager = this.hasManagerAuthority(user);
        if (post.getCategory() == Category.OFFICIAL) {
            return isManager;
        }

        boolean isAuthor = post.getAuthor().getId().equals(user.getId());

        return isAuthor || isManager;
    }

    public boolean isMyApplication(UserInfo userInfo, Long appId) {
        Application application = applicationRepository.findByIdWithUser(appId)
                .orElseThrow(() -> new NotFoundException(ApplicationErrorCode.NOT_FOUND_APPLICATION));
        Long userId = application.getUser().getId();
        return userId.equals(userInfo.getId()) || this.hasManagerAuthority(userInfo);
    }
}
