package com.snut_likelion.domain.user.service;

import com.snut_likelion.domain.user.dto.request.UpdateProfileRequest;
import com.snut_likelion.domain.user.entity.PortfolioLink;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.LionInfoRepository;
import com.snut_likelion.domain.user.repository.PortfolioLinkRepository;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.error.exception.NotFoundException;
import com.snut_likelion.global.provider.FileProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCommandService {

    private final UserRepository userRepository;
    private final LionInfoRepository lionInfoRepository;
    private final FileProvider fileProvider;
    private final PortfolioLinkRepository portfolioLinkRepository;

    @Transactional
    @PreAuthorize("@authChecker.isMe(#loginUser, #memberId)")
    public void updateProfile(UserInfo loginUser, Long memberId, UpdateProfileRequest req) {
        User user = userRepository.findWithLionUserById(memberId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));

        if (req.getProfileImage() != null) {
            this.removePrevProfileImage(user);
            String storedName = this.storeProfileImage(req.getProfileImage());
            // 트랜잭션이 롤백되면 방금 저장한 파일을 삭제하도록 등록
            fileProvider.setTransactionSynchronizationForImage(storedName);
            user.changeProfileImage(fileProvider.buildImageUrl(storedName)); // TODO: 프로필 이미지 URL 빌드 로직 수정
        }

        if (!req.getPortfolioLinks().isEmpty()) {
            this.connectPortfolioLinks(req.getPortfolioLinks(), user);
        }

        user.updateProfile(req.getIntro(), req.getDescription(), req.getMajor());
    }

    private void connectPortfolioLinks(List<UpdateProfileRequest.PortfolioLinkDto> portfolioLinkDtos, User user) {
        user.getPortfolioLinks().clear();
        List<PortfolioLink> portfolioLinkList = portfolioLinkDtos.stream()
                .map(UpdateProfileRequest.PortfolioLinkDto::toEntity)
                .toList();
        user.setPortfolioLinkList(portfolioLinkList);
        portfolioLinkRepository.saveAll(portfolioLinkList); // TODO: N+1 문제 해결
    }

    private String storeProfileImage(MultipartFile profileImage) {
        String contentType = profileImage.getContentType();

        if (!contentType.startsWith("image/")) {
            throw new BadRequestException(UserErrorCode.INVALID_PROFILE_IMAGE_FORMAT);
        }

        return fileProvider.storeFile(profileImage);
    }

//    @Transactional
//    @PreAuthorize("@authChecker.isMe(#loginUser, #memberId)")
//    public void upsertLionInfo(UserInfo loginUser, Long memberId, int generation, UpdateLionInfoRequest req) {
//        lionInfoRepository.findByUser_IdAndGeneration(memberId, generation)
//                .ifPresentOrElse(
//                        lionInfo -> {
//                            lionInfo.update(
//                                    req.getStacks(),
//                                    Part.valueOf(String.valueOf(req.getPart())),
//                                    Role.valueOf(String.valueOf(req.getRole()))
//                            );
//                        },
//                        () -> {
//                            LionInfo lionInfo = LionInfo.of(
//                                    generation,
//                                    Part.valueOf(String.valueOf(req.getPart())),
//                                    Role.valueOf(String.valueOf(req.getRole()))
//                            );
//
//                            User user = userRepository.findById(memberId)
//                                    .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
//                            user.addLionInfo(lionInfo);
//                            lionInfoRepository.save(lionInfo);
//                        }
//                );
//    }

    @Transactional
    @PreAuthorize("@authChecker.isMe(#loginUser, #memberId)")
    public void withdrawMember(UserInfo loginUser, Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
        this.removePrevProfileImage(user);
        userRepository.delete(user);
    }

    private void removePrevProfileImage(User user) {
        if (user.getProfileImageUrl() != null) {
            String profileImageName = fileProvider.extractImageName(user.getProfileImageUrl());
            fileProvider.deleteFile(profileImageName);
        }
    }
}
