package com.snut_likeliion.domain.user.service;


import com.snut_likeliion.domain.project.entity.Project;
import com.snut_likeliion.domain.project.entity.ProjectCategory;
import com.snut_likeliion.domain.project.entity.ProjectParticipation;
import com.snut_likeliion.domain.user.dto.response.LionInfoDetailsResponse;
import com.snut_likeliion.domain.user.dto.response.MemberDetailResponse;
import com.snut_likeliion.domain.user.dto.response.MemberResponse;
import com.snut_likeliion.domain.user.dto.response.MemberSearchResponse;
import com.snut_likeliion.domain.user.entity.*;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.domain.user.repository.LionInfoRepository;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.error.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LionInfoRepository lionInfoRepository;

    @InjectMocks
    private MemberQueryService memberQueryService;

    @Test
    void getMembersByQuery_whenManagerTrue_returnsManagersOnly() {
        int generation = 13;
        LionInfo adminLionInfo = LionInfo.builder()
                .generation(generation)
                .role(Role.ROLE_ADMIN)
                .part(Part.BACKEND)
                .build();

        User adminUser = User.builder()
                .username("admin")
                .profileImageUrl("http://example.com/admin.jpg")
                .intro("대표입니다.")
                .build();
        PortfolioLink github = PortfolioLink.builder()
                .name(PortFolioLinkType.GITHUB)
                .url("https://github.com/admin")
                .build();
        adminUser.setPortfolioLinkList(List.of(github));

        adminLionInfo.setUser(adminUser);

        LionInfo userLionInfo = LionInfo.builder()
                .generation(generation)
                .role(Role.ROLE_USER)
                .part(Part.BACKEND)
                .build();

        User normalUser = User.builder()
                .username("user")
                .profileImageUrl("http://example.com/user.jpg")
                .intro("아기사자입니다.")
                .build();
        userLionInfo.setUser(normalUser);

        when(lionInfoRepository.findAllByGeneration(generation))
                .thenReturn(List.of(adminLionInfo, userLionInfo));

        // Execute service
        List<MemberResponse> result = memberQueryService.getMembersByQuery(generation, true);

        // Verify only admin returned
        assertThat(result).hasSize(1);
        MemberResponse response = result.get(0);
        assertAll(
                () -> assertThat(response.getName()).isEqualTo("admin"),
                () -> assertThat(response.getProfileImageUrl()).isEqualTo("http://example.com/admin.jpg"),
                () -> assertThat(response.getGeneration()).isEqualTo(generation),
                () -> assertThat(response.getRole()).isEqualTo("대표"),
                () -> assertThat(response.getPart()).isEqualTo(Part.BACKEND),
                () -> assertThat(response.getIntro()).isEqualTo("대표입니다."),
                () -> assertThat(response.getPortfolioLinks()).hasSize(1),
                () -> assertThat(response.getPortfolioLinks().get(0).getName()).isEqualTo(PortFolioLinkType.GITHUB)
        );
    }

    @Test
    void getMembersByQuery_whenManagerFalse_returnsBabyOnly() {
        int generation = 13;
        LionInfo adminLionInfo = LionInfo.builder()
                .generation(generation)
                .role(Role.ROLE_ADMIN)
                .part(Part.BACKEND)
                .build();

        User adminUser = User.builder()
                .username("admin")
                .profileImageUrl("http://example.com/admin.jpg")
                .intro("대표입니다.")
                .build();
        PortfolioLink github = PortfolioLink.builder()
                .name(PortFolioLinkType.GITHUB)
                .url("https://github.com/admin")
                .build();
        adminUser.setPortfolioLinkList(List.of(github));

        adminLionInfo.setUser(adminUser);

        LionInfo userLionInfo = LionInfo.builder()
                .generation(generation)
                .role(Role.ROLE_USER)
                .part(Part.FRONTEND)
                .build();

        User normalUser = User.builder()
                .username("user")
                .profileImageUrl("http://example.com/user.jpg")
                .intro("아기사자입니다.")
                .build();
        userLionInfo.setUser(normalUser);

        when(lionInfoRepository.findAllByGeneration(generation))
                .thenReturn(List.of(adminLionInfo, userLionInfo));

        // Execute service
        List<MemberResponse> result = memberQueryService.getMembersByQuery(generation, false);

        // Verify only admin returned
        assertThat(result).hasSize(1);
        MemberResponse response = result.get(0);
        assertAll(
                () -> assertThat(response.getName()).isEqualTo("user"),
                () -> assertThat(response.getProfileImageUrl()).isEqualTo("http://example.com/user.jpg"),
                () -> assertThat(response.getGeneration()).isEqualTo(generation),
                () -> assertThat(response.getRole()).isEqualTo("아기사자"),
                () -> assertThat(response.getPart()).isEqualTo(Part.FRONTEND),
                () -> assertThat(response.getIntro()).isEqualTo("아기사자입니다."),
                () -> assertThat(response.getPortfolioLinks()).hasSize(0)
        );
    }

    @Test
    void getMemberDetailsById_whenExists_returnsMemberDetail() {
        Long memberId = 1L;
        User user = User.builder()
                .id(memberId)
                .email("test@test.com")
                .username("user")
                .profileImageUrl("http://example.com/user.jpg")
                .intro("아기사자입니다.")
                .description("설명입니다.")
                .saying("멋진 명언")
                .build();
        when(userRepository.findUserDetailsByUserId(memberId))
                .thenReturn(Optional.of(user));
        when(lionInfoRepository.findGenerationsByUser_Id(memberId))
                .thenReturn(List.of(11, 12, 13));

        MemberDetailResponse detail = memberQueryService.getMemberDetailsById(memberId);

        assertAll(
                () -> assertThat(detail.getId()).isEqualTo(memberId),
                () -> assertThat(detail.getEmail()).isEqualTo("test@test.com"),
                () -> assertThat(detail.getName()).isEqualTo("user"),
                () -> assertThat(detail.getProfileImageUrl()).isEqualTo("http://example.com/user.jpg"),
                () -> assertThat(detail.getIntro()).isEqualTo("아기사자입니다."),
                () -> assertThat(detail.getDescription()).isEqualTo("설명입니다."),
                () -> assertThat(detail.getPortfolioLinks()).hasSize(0),
                () -> assertThat(detail.getGenerations()).containsExactly(11, 12, 13)
        );
    }

    @Test
    void getMemberDetailsById_whenNotFound_throwsException() {
        Long memberId = 99L;
        when(userRepository.findUserDetailsByUserId(memberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberQueryService.getMemberDetailsById(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(UserErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    void getMemberLionInfoByIdAndGeneration_whenExists_returnsDetails() {
        Long memberId = 1L;
        int generation = 13;

        LionInfo userLionInfo = LionInfo.builder()
                .generation(generation)
                .role(Role.ROLE_USER)
                .part(Part.BACKEND)
                .stacks("Java, Spring")
                .build();
        User user = User.builder()
                .id(memberId)
                .email("test@test.com")
                .username("user")
                .profileImageUrl("http://example.com/user.jpg")
                .intro("아기사자입니다.")
                .description("설명입니다.")
                .saying("멋진 명언")
                .build();
        user.addLionInfo(userLionInfo);

        Project p1 = Project.builder()
                .id(1L)
                .name("Project 1")
                .intro("Intro 1")
                .description("Description 1")
                .generation(generation)
                .category(ProjectCategory.HACKATHON)
                .build();

        Project p2 = Project.builder()
                .id(2L)
                .name("Project 2")
                .intro("Intro 2")
                .description("Description 2")
                .generation(generation)
                .category(ProjectCategory.IDEATHON)
                .build();

        ProjectParticipation part1 = new ProjectParticipation(userLionInfo, p1);
        ProjectParticipation part2 = new ProjectParticipation(userLionInfo, p2);
        userLionInfo.addProjectParticipation(part1);
        userLionInfo.addProjectParticipation(part2);

        when(lionInfoRepository.findByUser_IdAndGeneration(memberId, generation))
                .thenReturn(Optional.of(userLionInfo));

        LionInfoDetailsResponse infoDetails = memberQueryService.getMemberLionInfoByIdAndGeneration(memberId, generation);

        assertAll(
                () -> assertThat(infoDetails.getGeneration()).isEqualTo(generation),
                () -> assertThat(infoDetails.getRole()).isEqualTo("아기사자"),
                () -> assertThat(infoDetails.getPart()).isEqualTo(Part.BACKEND.name()),
                () -> assertThat(infoDetails.getStacks()).hasSize(2),
                () -> assertThat(infoDetails.getStacks()).containsExactly("Java", "Spring"),
                () -> assertThat(infoDetails.getProjects()).hasSize(2),
                () -> assertThat(infoDetails.getProjects()).extracting("id", "name", "thumbnailUrl")
                        .containsExactly(
                                tuple(1L, "Project 1", null),
                                tuple(2L, "Project 2", null)
                        )
        );

    }

    @Test
    void getMemberLionInfoByIdAndGeneration_whenNotFound_throwsException() {
        Long memberId = 999L;
        int generation = 999;
        when(lionInfoRepository.findByUser_IdAndGeneration(memberId, generation))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberQueryService.getMemberLionInfoByIdAndGeneration(memberId, generation))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(UserErrorCode.NOT_FOUND_LION_INFO.getMessage());
    }

    @Test
    void searchMembers_delegatesToRepository_returnsResults() {
        // Given
        String keyword = "John";
        MemberSearchResponse msr1 = MemberSearchResponse.builder()
                .id(1L)
                .name("John")
                .part(Part.BACKEND)
                .generation(13)
                .profileImageUrl("url1")
                .build();

        MemberSearchResponse msr2 = MemberSearchResponse.builder()
                .id(2L)
                .name("Johnny")
                .part(Part.FRONTEND)
                .generation(12)
                .profileImageUrl("url2")
                .build();

        when(userRepository.searchUserByKeyword(keyword)).thenReturn(List.of(msr1, msr2));

        // When
        List<MemberSearchResponse> result = memberQueryService.searchMembers(keyword);

        // Then
        assertThat(result).containsExactly(msr1, msr2);
    }

    @Test
    void searchMembers_whenNoMatches_returnsEmptyList() {
        // Given
        String keyword = "xyz";
        when(userRepository.searchUserByKeyword(keyword)).thenReturn(List.of());

        // When
        List<MemberSearchResponse> result = memberQueryService.searchMembers(keyword);

        // Then
        assertThat(result).isEmpty();
    }
}
