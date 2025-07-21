package project.predix.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import project.predix.auth.JwtProperties;
import project.predix.auth.domain.RefreshToken;
import project.predix.auth.repository.RefreshTokenRepository;
import project.predix.member.domain.Member;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties props;

    @Transactional
    public RefreshToken create(Member member, String token){
        refreshTokenRepository.deleteByMember(member);
        RefreshToken refreshToken = new RefreshToken(
                member,
                token,
                Instant.now().plusMillis(props.refreshTtl())
        );
        return refreshTokenRepository.save(refreshToken);
    }

    public Member validateAndGetMember(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token "));

        if(refreshToken.isExpired())
            throw new IllegalStateException("Refresh token expired");

        return refreshToken.getMember();
    }

    @Transactional
    public void deleteByMember(Member member){
        refreshTokenRepository.deleteByMember(member);
    }
}
