package com.snut_likelion.domain.user.controller;

import com.snut_likelion.domain.user.dto.request.UpdateProfileRequest;
import com.snut_likelion.domain.user.dto.response.LionInfoDetailsResponse;
import com.snut_likelion.domain.user.dto.response.MemberDetailResponse;
import com.snut_likelion.domain.user.dto.response.MemberResponse;
import com.snut_likelion.domain.user.dto.response.MemberSearchResponse;
import com.snut_likelion.domain.user.service.MemberCommandService;
import com.snut_likelion.domain.user.service.MemberQueryService;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping
    public ApiResponse<List<MemberResponse>> getMembersByQuery(
            @RequestParam(name = "generation") int generation,
            @RequestParam(name = "isManager") boolean isManager
    ) {
        return ApiResponse.success(
                memberQueryService.getMembersByQuery(generation, isManager),
                "멤버 리스트 조회 성공"
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<MemberSearchResponse>> searchMembers(
            @RequestParam(name = "keyword") String keyword
    ) {
        return ApiResponse.success(
                memberQueryService.searchMembers(keyword),
                "멤버 검색 성공"
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<MemberDetailResponse> getMyDetails(
            @AuthenticationPrincipal SnutLikeLionUser loginUser
    ) {
        return ApiResponse.success(
                memberQueryService.getMyDetails(loginUser.getId()),
                "멤버 상세 정보 조회 성공"
        );
    }

    @GetMapping("/{memberId}")
    public ApiResponse<MemberDetailResponse> getMemberDetails(
            @PathVariable("memberId") Long memberId
    ) {
        return ApiResponse.success(memberQueryService.getMemberDetailsById(memberId), "멤버 상세 정보 조회 성공");
    }

    @GetMapping("/{memberId}/lion-info")
    public ApiResponse<LionInfoDetailsResponse> getMemberLionInfo(
            @PathVariable("memberId") Long memberId,
            @RequestParam(name = "generation") int generation
    ) {
        return ApiResponse.success(
                memberQueryService.getMemberLionInfoByIdAndGeneration(memberId, generation),
                String.format("UserId: %d | %d기 활동 정보 조회 성공", memberId, generation)
        );
    }

    @PatchMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateProfile(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("memberId") Long memberId,
            @ModelAttribute("updateProfileRequest") UpdateProfileRequest req
    ) {
        memberCommandService.updateProfile(loginUser.getUserInfo(), memberId, req);
    }

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawMember(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("memberId") Long memberId
    ) {
        memberCommandService.withdrawMember(loginUser.getUserInfo(), memberId);
    }
}
