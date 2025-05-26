package com.snut_likeliion.global.auth.checker;

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
}
