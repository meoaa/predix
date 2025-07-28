package project.predix.member.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.predix.common.ApiResponse;
import project.predix.member.domain.Member;
import project.predix.member.dto.ProfileUpdateRequestDto;
import project.predix.member.dto.ProfileUpdateResponseDto;
import project.predix.member.service.MemberService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/profile")
public class ProfileApiController {

    private final MemberService memberService;

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileUpdateResponseDto>> updateProfile(
            @RequestBody ProfileUpdateRequestDto requestDto,
            @AuthenticationPrincipal Member authenticatedMember){
        ProfileUpdateResponseDto profileUpdateResponseDto = memberService.updateProfile(requestDto, authenticatedMember.getId());

        return ResponseEntity.ok(ApiResponse.of(200, "성공적으로 수정되었습니다.", profileUpdateResponseDto));
    }
}
