package project.predix.member.dto;

import lombok.Data;
import project.predix.member.domain.Member;

import java.time.LocalDateTime;

@Data
public class ProfileResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;

    private ProfileResponseDto(Long id, String username, String email, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static ProfileResponseDto from(Member member){
        return new ProfileResponseDto(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getNickname(),
                member.getCreatedAt());
    }
}
