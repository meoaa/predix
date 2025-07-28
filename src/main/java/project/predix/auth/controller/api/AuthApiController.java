package project.predix.auth.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import project.predix.auth.JwtProvider;
import project.predix.auth.dto.LoginRequestDto;
import project.predix.auth.dto.SignUpRequestDto;
import project.predix.auth.dto.SignUpResponseDto;
import project.predix.auth.dto.TokenResponseDto;
import project.predix.auth.service.RefreshTokenService;
import project.predix.common.ApiResponse;
import project.predix.member.domain.Member;
import project.predix.member.domain.Role;

import project.predix.member.service.MemberService;

import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    private final RefreshTokenService refreshTokenService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signup(@Valid @RequestBody SignUpRequestDto dto){
        SignUpResponseDto signUpResponseDto = memberService.create(dto);

        return ResponseEntity.ok(ApiResponse.of(200,"회원가입이 완료되었습니다.", signUpResponseDto));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@Valid @RequestBody LoginRequestDto dto){

        Authentication authenticate = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        UserDetails user = (UserDetails) authenticate.getPrincipal();

        String access = jwtProvider.generateAccess(user.getUsername(), user.getAuthorities());
        String refresh = jwtProvider.generateRefresh(user.getUsername());

        refreshTokenService.createOrUpdateRefreshToken((Member) user, refresh);

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", access)
                .httpOnly(true).secure(true).path("/")
                .maxAge(Duration.ofMillis(jwtProvider.getAccessTtl()))
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.of(200,"로그인 되었습니다.", new TokenResponseDto(access,refresh)));

    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponseDto>> refresh(@RequestBody String refreshToken){
        Member member = refreshTokenService.validateAndGetMember(refreshToken);

        String newAccess = jwtProvider.generateAccess(
                member.getUsername(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_USER.toString()))
        );

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", newAccess)
                .httpOnly(true).secure(true).path("/")
                .maxAge(Duration.ofMillis(jwtProvider.getAccessTtl()))
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.of(200, "토큰 재발급 완료", new TokenResponseDto(newAccess, refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication){
        if(authentication != null && authentication.getPrincipal() instanceof Member member){
            refreshTokenService.deleteByMember(member);
        }
        // 2) 쿠키 즉시 만료
        ResponseCookie clearAccess = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true).secure(true).path("/")
                .maxAge(0)                 // 👉 바로 삭제
                .sameSite("None")
                .build();

        // (refresh 토큰을 쿠키에 두고 있다면 같은 방식으로 하나 더)
        ResponseCookie clearRefresh = ResponseCookie.from("REFRESH_TOKEN", "")
                .httpOnly(true).secure(true).path("/")
                .maxAge(0).sameSite("None").build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearAccess.toString())
                .header(HttpHeaders.SET_COOKIE, clearRefresh.toString())
                .body(ApiResponse.of(200,"로그아웃 완료", null));
    }


    @GetMapping("/check/username")
    public ResponseEntity<?> checkUsername(@RequestParam String username){
        String checkedUsername = memberService.checkUsername(username);
        return ResponseEntity.ok(checkedUsername);
    }

    @GetMapping("/check/email")
    public ResponseEntity<?> checkEmail(@RequestParam String email){
        String checkedEmail = memberService.checkDuplicateEmail(email);
        return ResponseEntity.ok(checkedEmail);
    }
}
