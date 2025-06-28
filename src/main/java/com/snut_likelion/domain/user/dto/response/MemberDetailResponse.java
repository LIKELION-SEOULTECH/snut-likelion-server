package com.snut_likelion.domain.user.dto.response;

import com.snut_likelion.domain.user.entity.PortfolioLink;
import com.snut_likelion.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetailResponse {

    private Long id;
    private String name;
    private String profileImageUrl;
    private String intro;
    private String description;
    private List<PortfolioLinkDto> portfolioLinks;
    private String email;
    private List<Integer> generations;

    @Builder
    public MemberDetailResponse(Long id, String name, String profileImageUrl, String intro, String description, List<PortfolioLink> portfolioLinks, String email, List<Integer> generations) {
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.intro = intro;
        this.description = description;
        this.portfolioLinks = portfolioLinks.stream().map(PortfolioLinkDto::from).toList();
        this.email = email;
        this.generations = generations;
    }

    public static MemberDetailResponse of(User member, List<Integer> generations) {
        return MemberDetailResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getUsername())
                .profileImageUrl(member.getProfileImageUrl())
                .intro(member.getIntro())
                .description(member.getDescription())
                .portfolioLinks(member.getPortfolioLinks())
                .generations(generations)
                .build();
    }

}
