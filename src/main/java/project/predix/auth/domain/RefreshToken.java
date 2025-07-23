package project.predix.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.predix.member.domain.Member;

import java.time.Instant;
import java.time.LocalDateTime;

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

    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RefreshToken(Member member, String token, Instant expiry) {
        this.member = member;
        this.token = token;
        this.expiry = expiry;
        this.createAt = LocalDateTime.now();
    }

    public boolean isExpired(){
        return expiry.isBefore(Instant.now());
    }
    public void updateToken(String token, Instant expiry){
        this.token = token;
        this.expiry = expiry;
        this.updatedAt = LocalDateTime.now();
    }

}
