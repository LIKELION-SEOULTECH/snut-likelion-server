package com.snut_likeliion.domain.user.service;

import com.snut_likeliion.domain.project.infra.FileProvider;
import com.snut_likeliion.domain.user.dto.request.UpdateLionInfoRequest;
import com.snut_likeliion.domain.user.dto.request.UpdateProfileRequest;
import com.snut_likeliion.domain.user.entity.LionInfo;
import com.snut_likeliion.domain.user.entity.Part;
import com.snut_likeliion.domain.user.entity.Role;
import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.domain.user.repository.LionInfoRepository;
import com.snut_likeliion.domain.user.repository.PortfolioLinkRepository;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.auth.model.UserInfo;
import com.snut_likeliion.global.error.exception.BadRequestException;
import com.snut_likeliion.global.error.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LionInfoRepository lionInfoRepository;

    @Mock
    private FileProvider fileProvider;

    @Mock
    private PortfolioLinkRepository portfolioLinkRepository;

    @InjectMocks
    private MemberCommandService memberCommandService;

    @Mock
    private UserInfo loginUser;

    @Test
    void updateProfile_withImageAndLinks_updatesImageAndLinksAndProfile() {
        // Given
        Long memberId = 1L;
        User user = spy(User.builder()
                .id(memberId)
                .profileImageUrl("http://cdn/old.png")
                .build());
        when(userRepository.findWithLionUserById(memberId))
                .thenReturn(Optional.of(user));

        // Prepare request
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("image/png");
        UpdateProfileRequest.PortfolioLinkDto pl1 = new UpdateProfileRequest.PortfolioLinkDto("GITHUB", "https://github.com/example");
        UpdateProfileRequest.PortfolioLinkDto pl2 = new UpdateProfileRequest.PortfolioLinkDto("NOTION", "https://notion.com/example");

        UpdateProfileRequest req = UpdateProfileRequest.builder()
                .profileImage(image)
                .intro("새로운 소개")
                .description("새로운 설명")
                .major("컴퓨터공학")
                .portfolioLinks(List.of(pl1, pl2))
                .build();

        // FileProvider behavior
        when(fileProvider.extractImageName(anyString())).thenReturn("old.png");
        when(fileProvider.storeFile(image)).thenReturn("new.png");
        when(fileProvider.buildImageUrl("new.png")).thenReturn("http://cdn/new.png");

        // When
        TransactionSynchronizationManager.initSynchronization();
        memberCommandService.updateProfile(loginUser, memberId, req);
        TransactionSynchronizationManager.clearSynchronization();

        // Then
        assertAll(
                () -> verify(fileProvider).extractImageName("http://cdn/old.png"),
                () -> verify(fileProvider).storeFile(image),
                () -> verify(fileProvider).buildImageUrl("new.png"),
                () -> verify(user).changeProfileImage("http://cdn/new.png"),
                () -> verify(portfolioLinkRepository).saveAll(anyList()),
                () -> verify(user).setPortfolioLinkList(anyList()),
                () -> verify(portfolioLinkRepository).saveAll(anyList()),
                () -> verify(user).updateProfile("새로운 소개", "새로운 설명", "컴퓨터공학")
        );
    }

    @Test
    void updateProfile_withoutImageAndLinks_onlyUpdatesProfile() {
        Long memberId = 1L;
        User user = User.builder().id(memberId).build();
        when(userRepository.findWithLionUserById(memberId))
                .thenReturn(Optional.of(user));

        UpdateProfileRequest req = UpdateProfileRequest.builder()
                .intro("새로운 소개")
                .description("새로운 설명")
                .major("컴퓨터공학")
                .build();


        memberCommandService.updateProfile(loginUser, memberId, req);

        assertAll(
                () -> verify(fileProvider, never()).storeFile(any()),
                () -> verify(portfolioLinkRepository, never()).saveAll(any()),
                () -> assertThat(user.getIntro()).isEqualTo("새로운 소개"),
                () -> assertThat(user.getDescription()).isEqualTo("새로운 설명"),
                () -> assertThat(user.getMajor()).isEqualTo("컴퓨터공학")
        );
    }

    @Test
    void updateProfile_invalidImageFormat_throwsBadRequest() {
        Long memberId = 1L;
        User user = spy(User.builder().id(memberId).build());
        when(userRepository.findWithLionUserById(memberId))
                .thenReturn(Optional.of(user));

        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("application/pdf");
        UpdateProfileRequest req = UpdateProfileRequest.builder()
                .profileImage(image)
                .intro("새로운 소개")
                .description("새로운 설명")
                .major("컴퓨터공학")
                .portfolioLinks(List.of())
                .build();

        assertThatThrownBy(() -> memberCommandService.updateProfile(loginUser, memberId, req))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(UserErrorCode.INVALID_PROFILE_IMAGE_FORMAT.getMessage());
    }

    @Test
    void upsertLionInfo_existing_shouldUpdateLionInfo() {
        // Given
        Long memberId = 1L;
        int generation = 13;
        UpdateLionInfoRequest req = UpdateLionInfoRequest.builder()
                .stacks(List.of("Java", "Spring"))
                .part(UpdateLionInfoRequest.PartMapping.BACKEND)
                .role(UpdateLionInfoRequest.RoleMapping.ROLE_USER)
                .build();

        LionInfo existingLionInfo = LionInfo.builder()
                .generation(3)
                .part(Part.FRONTEND)
                .role(Role.ROLE_ADMIN)
                .build();

        when(lionInfoRepository.findByUser_IdAndGeneration(memberId, generation))
                .thenReturn(Optional.of(existingLionInfo));

        // When
        memberCommandService.upsertLionInfo(loginUser, memberId, generation, req);

        // Then
        assertAll(
                () -> assertThat(existingLionInfo.getGeneration()).isEqualTo(3),
                () -> assertThat(existingLionInfo.getPart()).isEqualTo(Part.BACKEND),
                () -> assertThat(existingLionInfo.getRole()).isEqualTo(Role.ROLE_USER),
                () -> assertThat(existingLionInfo.getStacks()).isEqualTo("Java, Spring"),
                () -> verify(lionInfoRepository, never()).save(any())
        );
    }

    @Test
    void upsertLionInfo_new_shouldSaveNewLionInfo() {
        // Given
        Long memberId = 1L;
        int generation = 13;
        UpdateLionInfoRequest req = UpdateLionInfoRequest.builder()
                .stacks(List.of("Java", "Spring"))
                .part(UpdateLionInfoRequest.PartMapping.BACKEND)
                .role(UpdateLionInfoRequest.RoleMapping.ROLE_MANAGER)
                .build();

        when(lionInfoRepository.findByUser_IdAndGeneration(memberId, generation))
                .thenReturn(Optional.empty());

        User user = User.builder().id(memberId).build();
        when(userRepository.findById(memberId))
                .thenReturn(Optional.of(user));

        // When
        memberCommandService.upsertLionInfo(loginUser, memberId, generation, req);

        // Then
        LionInfo saved = user.getLionInfos().get(0);
        assertThat(saved.getGeneration()).isEqualTo(generation);
        assertThat(saved.getPart()).isEqualTo(Part.BACKEND);
        assertThat(saved.getRole()).isEqualTo(Role.ROLE_MANAGER);
        assertThat(saved.getUser()).isEqualTo(user);
    }

    @Test
    void withdrawMember_withImage_shouldDeleteImageAndUser() {
        // Given
        Long memberId = 1L;
        User user = spy(User.builder()
                .id(memberId)
                .profileImageUrl("http://cdn/avatar.png")
                .build());

        when(userRepository.findById(memberId))
                .thenReturn(Optional.of(user));
        when(fileProvider.extractImageName("http://cdn/avatar.png"))
                .thenReturn("avatar.png");

        // when
        memberCommandService.withdrawMember(loginUser, memberId);

        // then
        verify(fileProvider).deleteFile("avatar.png");
        verify(userRepository).delete(user);
    }

    @Test
    void withdrawMember_withoutImage_shouldDeleteUserOnly() {
        // Given
        Long memberId = 1L;
        User user = spy(User.builder().id(memberId).build());
        when(userRepository.findById(memberId))
                .thenReturn(Optional.of(user));

        // When
        memberCommandService.withdrawMember(loginUser, memberId);

        // Then
        verify(fileProvider, never()).deleteFile(anyString());
        verify(userRepository).delete(user);
    }

    @Test
    void updateProfile_userNotFound_throwsNotFound() {
        // Given
        Long memberId = 1L;
        when(userRepository.findWithLionUserById(memberId))
                .thenReturn(Optional.empty());
        UpdateProfileRequest req = mock(UpdateProfileRequest.class);

        // When & Then
        assertThatThrownBy(() -> memberCommandService.updateProfile(loginUser, memberId, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(UserErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    void withdrawMember_userNotFound_throwsNotFound() {
        // Given
        Long memberId = 1L;
        when(userRepository.findById(memberId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberCommandService.withdrawMember(loginUser, memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(UserErrorCode.NOT_FOUND.getMessage());
    }
}