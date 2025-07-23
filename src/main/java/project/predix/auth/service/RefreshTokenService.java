package project.predix.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.auth.JwtProperties;
import project.predix.auth.domain.RefreshToken;
import project.predix.auth.repository.RefreshTokenRepository;
import project.predix.member.domain.Member;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties props;

    @Transactional
    public void createOrUpdateRefreshToken(Member member, String token){
        Optional<RefreshToken> foundToken = refreshTokenRepository.findByMember(member);
        RefreshToken refreshToken;
        if(foundToken.isPresent()){
            refreshToken = foundToken.get();
            refreshToken.updateToken(token ,Instant.now().plusMillis(props.refreshTtl()));
        }else{
            refreshToken = new RefreshToken(member, token, Instant.now().plusMillis(props.refreshTtl()));
            refreshTokenRepository.save(refreshToken);
        }

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
