package com.snut_likelion.admin.member.service;

import com.snut_likelion.admin.member.dto.request.UpdateMemberRequest;
import com.snut_likelion.admin.member.dto.response.MemberPageResponse;
import com.snut_likelion.admin.member.infra.AdminMemberQueryRepository;
import com.snut_likelion.domain.user.entity.LionInfo;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.Role;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.error.exception.NotFoundException;
import com.snut_likelion.global.provider.FileProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final int PAGE_SIZE = 10;

    private final UserRepository userRepository;
    private final AdminMemberQueryRepository queryRepository;
    private final FileProvider fileProvider;

    public MemberPageResponse getMemberList(Integer generation, Part part, Role role, int page, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<MemberPageResponse.MemberListResponse> result =
                queryRepository.getMemberList(generation, part, role, keyword, pageRequest);
        return MemberPageResponse.from(result);
    }

    @Transactional
    public void updateMember(Long memberId, int generation, UpdateMemberRequest req) {
        User user = userRepository.findWithLionUserById(memberId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));

        LionInfo targetLionInfo = user.getLionInfos().stream()
                .filter(lionInfo -> lionInfo.getGeneration() == generation)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_LION_INFO));

        user.updateUsername(req.getUsername());
        targetLionInfo.updateByAdmin(
                req.getPart(),
                req.getRole(),
                req.getDepartment()
        );
    }

    @Transactional
    public void deleteMember(Long memberId) {
        userRepository.findById(memberId)
                .ifPresentOrElse(
                        user -> {
                            this.removePrevProfileImage(user);
                            userRepository.delete(user);
                        },
                        () -> {
                            throw new NotFoundException(UserErrorCode.NOT_FOUND);
                        }
                );
    }

    private void removePrevProfileImage(User user) {
        if (user.getProfileImageUrl() != null) {
            String profileImageName = fileProvider.extractImageName(user.getProfileImageUrl());
            fileProvider.deleteFile(profileImageName);
        }
    }
}
