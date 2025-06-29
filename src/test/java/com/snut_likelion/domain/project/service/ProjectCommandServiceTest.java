package com.snut_likelion.domain.project.service;

import com.snut_likelion.domain.project.dto.request.CreateProjectRequest;
import com.snut_likelion.domain.project.dto.request.RetrospectionDto;
import com.snut_likelion.domain.project.dto.request.UpdateProjectRequest;
import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectCategory;
import com.snut_likelion.domain.project.entity.ProjectParticipation;
import com.snut_likelion.domain.project.entity.ProjectRetrospection;
import com.snut_likelion.domain.project.exception.ProjectErrorCode;
<<<<<<< HEAD
=======
import com.snut_likelion.global.provider.FileProvider;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.project.infra.ProjectRepository;
import com.snut_likelion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likelion.domain.user.entity.LionInfo;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.Role;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.repository.LionInfoRepository;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.provider.FileProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectCommandServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private FileProvider fileProvider;
    @Mock
    private LionInfoRepository lionInfoRepository;
    @Mock
    private ProjectRetrospectionRepository projectRetrospectionRepository;

    @InjectMocks
    private ProjectCommandService projectCommandService;

    @Mock
    private UserInfo loginUser;

    @Test
    void create_withValidData_savesProjectWithAllRelations() {
        // Given
        int generation = 13;
        MultipartFile img = mock(MultipartFile.class);
        when(img.getContentType()).thenReturn("image/png");
        when(fileProvider.storeFile(img)).thenReturn("stored.png");
        doNothing().when(fileProvider).setTransactionSynchronizationForImage("stored.png");
        when(fileProvider.buildImageUrl("stored.png")).thenReturn("url.png");

        // retrospections
        User writer = User.builder()
                .id(1L)
                .username("writer")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(writer));
        RetrospectionDto rd = RetrospectionDto.builder()
                .memberId(1L)
                .content("content")
                .build();

        // participants
        LionInfo lionInfo = LionInfo.of(generation, Part.BACKEND, Role.ROLE_USER);
        when(lionInfoRepository.findByUser_IdAndGeneration(1L, generation))
                .thenReturn(Optional.of(lionInfo));

        // Prepare spy request and project
        Project spyProject = spy(Project.builder()
                .name("Test Project")
                .intro("This is a test project")
                .description("Detailed description of the test project")
                .generation(generation)
                .category(ProjectCategory.HACKATHON)
                .websiteUrl("http://example.com")
                .build());

        CreateProjectRequest realReq = CreateProjectRequest.builder()
                .name(spyProject.getName())
                .intro(spyProject.getIntro())
                .description(spyProject.getDescription())
                .generation(spyProject.getGeneration())
                .category(spyProject.getCategory())
                .websiteUrl(spyProject.getWebsiteUrl())
                .tags(List.of("tag1", "tag2"))
                .images(List.of(img))
                .retrospections(List.of(rd))
                .build();
        CreateProjectRequest spyReq = spy(realReq);
        doReturn(spyProject).when(spyReq).toEntityWithValue();

        // When
        projectCommandService.create(spyReq);

        // Then
        assertAll(
                () -> verify(fileProvider).storeFile(img),
                () -> verify(fileProvider).setTransactionSynchronizationForImage("stored.png"),
                () -> verify(fileProvider).buildImageUrl("stored.png"),
                () -> assertThat(spyProject.getName()).isEqualTo("Test Project"),
                () -> assertThat(spyProject.getIntro()).isEqualTo("This is a test project"),
                () -> assertThat(spyProject.getDescription()).isEqualTo("Detailed description of the test project"),
                () -> assertThat(spyProject.getGeneration()).isEqualTo(generation),
                () -> assertThat(spyProject.getCategory()).isEqualTo(ProjectCategory.HACKATHON),
                () -> assertThat(spyProject.getWebsiteUrl()).isEqualTo("http://example.com"),
                () -> assertThat(spyProject.getImageUrlList()).containsExactly("url.png"),
                () -> assertThat(spyProject.getTagList()).containsExactly("TAG1", "TAG2"),
                () -> assertThat(spyProject.getParticipations()).hasSize(1),
                () -> assertThat(spyProject.getRetrospections()).hasSize(1)
        );
    }

    @Test
    void create_withInvalidImageFormat_throwsBadRequest() {
        // Given
        MultipartFile img = mock(MultipartFile.class);
        when(img.getContentType()).thenReturn("application/pdf");
        CreateProjectRequest req = CreateProjectRequest.builder()
                .name("name")
                .intro("intro")
                .description("desc")
                .generation(13)
                .category(ProjectCategory.HACKATHON)
                .websiteUrl("url")
                .tags(List.of())
                .images(List.of(img))
                .retrospections(List.of())
                .build();

        assertThatThrownBy(() -> projectCommandService.create(req))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ProjectErrorCode.INVALID_FILE_FORMAT.getMessage());
    }

    @Test
    void modify_updatesFieldsAndRelationsAndImagesAndRetrospections() {
        Long id = 1L;

        Project oldProject = Project.builder()
                .id(id)
                .name("oldName")
                .intro("oldIntro")
                .description("oldDesc")
                .generation(10)
                .category(ProjectCategory.HACKATHON)
                .websiteUrl("http://old.com")
                .build();
        oldProject.setTags(List.of("oldTag"));
        oldProject.addImage(List.of("oldImage"));

        when(projectRepository.findById(id)).thenReturn(Optional.of(oldProject));
//
//        // participants
//        LionInfo li = LionInfo.of(13, Part.BACKEND, Role.ROLE_USER);
//        User writer = User.builder()
//                .id(3L)
//                .username("writer")
//                .build();
//        writer.addLionInfo(li);
//
//        ProjectParticipation p1 = new ProjectParticipation(li, oldProject);
//        oldProject.addParticipation(p1);
//
//        when(lionInfoRepository.findByUser_IdAndGeneration(3L, 14))
//                .thenReturn(Optional.of(li));

        // retrospections: existing and new
        long existingRetMemberId = 2L;
        RetrospectionDto existingDto = RetrospectionDto.builder()
                .memberId(existingRetMemberId)
                .content("updatedContent")
                .build();
        ProjectRetrospection existingRet = new ProjectRetrospection("existingContent");
        existingRet.setWriter(User.builder().id(existingRetMemberId).build());
        oldProject.addRetrospection(existingRet);
        LionInfo li2 = LionInfo.of(14, Part.FRONTEND, Role.ROLE_USER);
        oldProject.addParticipation(new ProjectParticipation(li2, oldProject));
        when(projectRetrospectionRepository
                .findByWriter_IdAndProject_Id(existingRetMemberId, id))
                .thenReturn(Optional.of(existingRet));

        RetrospectionDto newDto = RetrospectionDto.builder()
                .memberId(4L)
                .content("newContent")
                .build();
        when(userRepository.findById(4L))
                .thenReturn(Optional.of(User.builder().id(4L).build()));

        LionInfo li4 = LionInfo.of(14, Part.BACKEND, Role.ROLE_USER);
        when(lionInfoRepository.findByUser_IdAndGeneration(4L, 14))
                .thenReturn(Optional.of(li4));
        // images
        MultipartFile img = mock(MultipartFile.class);
        when(img.getContentType()).thenReturn("image/jpeg");
        when(fileProvider.storeFile(img)).thenReturn("mod.png");
        doNothing().when(fileProvider).setTransactionSynchronizationForImage("mod.png");
        when(fileProvider.buildImageUrl("mod.png")).thenReturn("modUrl");

        UpdateProjectRequest req = UpdateProjectRequest.builder()
                .name("updatedName")
                .intro("updatedIntro")
                .description("updatedDesc")
                .generation(14)
                .category(ProjectCategory.IDEATHON)
                .tags(List.of("newTag"))
                .newImages(List.of(img))
                .retrospections(List.of(existingDto, newDto))
                .build();
        // When
        projectCommandService.modify(id, req);

        // Then
        assertAll(
                () -> verify(fileProvider).storeFile(img),
                () -> verify(fileProvider).setTransactionSynchronizationForImage("mod.png"),
                () -> verify(fileProvider).buildImageUrl("mod.png"),
                () -> assertThat(oldProject.getName()).isEqualTo("updatedName"),
                () -> assertThat(oldProject.getIntro()).isEqualTo("updatedIntro"),
                () -> assertThat(oldProject.getDescription()).isEqualTo("updatedDesc"),
                () -> assertThat(oldProject.getGeneration()).isEqualTo(14),
                () -> assertThat(oldProject.getCategory()).isEqualTo(ProjectCategory.IDEATHON),
                () -> assertThat(oldProject.getWebsiteUrl()).isEqualTo("http://old.com"),
                () -> assertThat(oldProject.getTagList()).containsExactly("NEWTAG"),
                () -> assertThat(oldProject.getImageUrlList()).containsExactly("oldImage", "modUrl"),
                () -> assertThat(oldProject.getParticipations()).hasSize(2),
                () -> assertThat(oldProject.getRetrospections()).hasSize(2),
                () -> assertThat(oldProject.getRetrospections()).extracting(ProjectRetrospection::getContent)
                        .containsExactly("updatedContent", "newContent")
        );
    }

    @Test
    void remove_deletesProjectAndFiles() {
        // Given
        Long id = 1L;
        Project project = Project.builder()
                .id(id)
                .name("oldName")
                .intro("oldIntro")
                .description("oldDesc")
                .generation(10)
                .category(ProjectCategory.HACKATHON)
                .websiteUrl("http://old.com")
                .build();
        project.setImages(List.of("http://cdn/a.png", "http://cdn/b.jpg"));
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(fileProvider.extractImageName("http://cdn/a.png")).thenReturn("a.png");
        when(fileProvider.extractImageName("http://cdn/b.jpg")).thenReturn("b.jpg");

        // When
        projectCommandService.remove(id);

        // Then
        verify(projectRepository).delete(project);
        verify(fileProvider).deleteFile("a.png");
        verify(fileProvider).deleteFile("b.jpg");
    }

    @Test
    void removeImage_removesFromProjectAndDeletesFile() {
        // Given
        Long id = 1L;
        Project project = Project.builder()
                .id(id)
                .name("oldName")
                .intro("oldIntro")
                .description("oldDesc")
                .generation(10)
                .category(ProjectCategory.HACKATHON)
                .websiteUrl("http://old.com")
                .build();
        project.setImages(List.of("http://cdn/x.png", "http://cdn/y.png"));
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(fileProvider.extractImageName("http://cdn/x.png")).thenReturn("x.png");

        // When
        projectCommandService.removeImage(id, "http://cdn/x.png");

        // Then
        assertThat(project.getImageUrlList()).containsExactly("http://cdn/y.png");
        verify(fileProvider).deleteFile("x.png");
    }
}