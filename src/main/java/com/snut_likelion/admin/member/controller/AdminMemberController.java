package com.snut_likelion.admin.member.controller;

import com.snut_likelion.admin.member.dto.request.UpdateMemberRequest;
import com.snut_likelion.admin.member.dto.response.MemberPageResponse;
import com.snut_likelion.admin.member.service.AdminMemberService;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.Role;
import com.snut_likelion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping
    public ApiResponse<MemberPageResponse> getMemberList(
            @RequestParam(value = "generation", required = false) Integer generation,
            @RequestParam(value = "part", required = false) Part part,
            @RequestParam(value = "role", required = false) Role role,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return ApiResponse.success(
                adminMemberService.getMemberList(generation, part, role, page, keyword),
                "멤버 리스트 조회 성공"
        );
    }

    @PutMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(
            @PathVariable Long memberId,
            @RequestParam("generation") int generation,
            @RequestBody UpdateMemberRequest req
    ) {
        adminMemberService.updateMember(memberId, generation, req);
    }

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Long memberId) {
        adminMemberService.deleteMember(memberId);
    }

}
