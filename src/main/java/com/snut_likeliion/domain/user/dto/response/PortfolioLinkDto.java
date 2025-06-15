package com.snut_likeliion.domain.user.dto.response;

import com.snut_likeliion.domain.user.entity.PortFolioLinkType;
import com.snut_likeliion.domain.user.entity.PortfolioLink;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PortfolioLinkDto {

    private PortFolioLinkType name;
    private String url;

    @Builder
    public PortfolioLinkDto(PortFolioLinkType name, String url) {
        this.name = name;
        this.url = url;
    }

    public static PortfolioLinkDto from(PortfolioLink portfolioLink) {
        return PortfolioLinkDto.builder()
                .name(portfolioLink.getName())
                .url(portfolioLink.getUrl())
                .build();
    }
}
