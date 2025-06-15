package com.snut_likeliion.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateLionInfoRequest {

    @Min(value = 1, message = "기수가 올바르지 않습니다.")
    private int generation;
    private PartMapping part;
    private RoleMapping role;
    private List<String> stacks = new ArrayList<>();

    @Builder
    public UpdateLionInfoRequest(int generation, PartMapping part, RoleMapping role, List<String> stacks) {
        this.generation = generation;
        this.part = part;
        this.role = role;
        this.stacks = stacks == null ? new ArrayList<>() : stacks;
    }

    @RequiredArgsConstructor
    public enum RoleMapping {
        @JsonProperty("아기사자")
        ROLE_USER("아기사자"),
        @JsonProperty("운영진")
        ROLE_MANAGER("운영진"),
        @JsonProperty("대표")
        ROLE_ADMIN("대표");

        private final String name;
    }

    @RequiredArgsConstructor
    public enum PartMapping {
        @JsonProperty("기획")
        PLANNING("기획"),
        @JsonProperty("디자인")
        DESIGN("디자인"),
        @JsonProperty("프론트엔드")
        FRONTEND("프론트엔드"),
        @JsonProperty("백엔드")
        BACKEND("백엔드"),
        @JsonProperty("AI")
        AI("AI");

        private final String name;
    }
}
