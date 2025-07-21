package project.predix.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.auth.domain.RefreshToken;
import project.predix.member.domain.Member;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByMember(Member member);
    void deleteByMember(Member member);
}
