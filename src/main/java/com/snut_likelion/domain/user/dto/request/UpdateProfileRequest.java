package com.snut_likelion.domain.user.dto.request;

import com.snut_likelion.domain.user.entity.PortFolioLinkType;
import com.snut_likelion.domain.user.entity.PortfolioLink;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProfileRequest {

    private MultipartFile profileImage;
    private String intro;
    private String description;
    private String saying;
    private String major;
    private List<String> stacks;
    private List<PortfolioLinkDto> portfolioLinks = new ArrayList<>();

    @Builder
    public UpdateProfileRequest(MultipartFile profileImage, String intro, String description, String saying, String major, List<String> stacks, List<PortfolioLinkDto> portfolioLinks) {
        this.profileImage = profileImage;
        this.intro = intro;
        this.description = description;
        this.saying = saying;
        this.major = major;
        this.stacks = stacks;
        this.portfolioLinks = portfolioLinks == null ? new ArrayList<>() : portfolioLinks;
    }

    @Getter
    @NoArgsConstructor
    public static class PortfolioLinkDto {

        private String name;
        private String url;

        @Builder
        public PortfolioLinkDto(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public PortfolioLink toEntity() {
            return PortfolioLink.builder()
                    .name(PortFolioLinkType.valueOf(name))
                    .url(url)
                    .build();
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
