package com.snut_likeliion.global.auth.checker;

import com.snut_likeliion.domain.blog.entity.BlogPost;
import com.snut_likeliion.domain.blog.entity.Category;
import com.snut_likeliion.domain.blog.exception.BlogErrorCode;
import com.snut_likeliion.domain.blog.repository.BlogPostRepository;
import com.snut_likeliion.domain.project.entity.ProjectRetrospection;
import com.snut_likeliion.domain.project.exception.ProjectErrorCode;
import com.snut_likeliion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likeliion.global.auth.model.UserInfo;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("authChecker")
@RequiredArgsConstructor
public class AuthChecker {

    private final ProjectRetrospectionRepository projectRetrospectionRepository;
    private final BlogPostRepository blogPostRepository;

    public boolean isMyRetrospection(UserInfo user, Long retrospectionId) {
        ProjectRetrospection retrospection = projectRetrospectionRepository.findById(retrospectionId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_RETROSPECTION));

        return this.isWriter(user, retrospection) || this.hasManagerAuthority(user);
    }

    private boolean hasManagerAuthority(UserInfo user) {
        return List.of("ROLE_ADMIN", "ROLE_MANAGER").contains(user.getRole());
    }

    private boolean isWriter(UserInfo user, ProjectRetrospection retrospection) {
        return retrospection.getWriter().getId().equals(user.getId());
    }

    public boolean checkIsOfficialAndManager(Category category, UserInfo user) {
        if (category == Category.OFFICIAL && !this.hasManagerAuthority(user)) {
            return false;
        }

        return true;
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
}
