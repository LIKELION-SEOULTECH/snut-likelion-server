
package com.snut_likelion.global.auth.checker;

import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.domain.blog.repository.BlogPostRepository;
import com.snut_likelion.domain.project.infra.ProjectParticipationRepository;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
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
    private final BlogPostRepository blogPostRepository;

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

    public boolean checkIsOfficialAndManager(Category category, UserInfo user) {
        if (category == Category.OFFICIAL && !this.hasManagerAuthority(user)) {
            return false;
        }

        return true;
    }

    public boolean checkIsOfficialAndManager(Category category, SnutLikeLionUser principal) {
        return checkIsOfficialAndManager(category, principal.getUserInfo());
    }

    // 수정 & 삭제 권한 검사 (OFFICIAL 게시글은 관리자만 / UNOFFICIAL 게시글은 작성자 또는 관리자)
    public boolean checkCanModify(Long postId, UserInfo user) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        boolean isManager = hasManagerAuthority(user);
        if (post.getCategory() == Category.OFFICIAL) {
            return isManager;
        }

        boolean isAuthor = post.getAuthor().getId().equals(user.getId());

        return isAuthor || isManager;
    }

    public boolean checkCanModify(Long postId, SnutLikeLionUser principal) {
        return checkCanModify(postId, principal.getUserInfo());
    }
}
