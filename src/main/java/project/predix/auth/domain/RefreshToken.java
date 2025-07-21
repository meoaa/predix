package project.predix.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.predix.member.domain.Member;

import java.time.Instant;

@Entity
@Table(name = "tb_refresh_tokens")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 500, nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiry;

    public RefreshToken(Member member, String token, Instant expiry) {
        this.member = member;
        this.token = token;
        this.expiry = expiry;
    }

    public boolean isExpired(){
        return expiry.isBefore(Instant.now());
    }

}
