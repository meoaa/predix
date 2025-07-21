package project.predix.auth.dto;

import lombok.Data;
import project.predix.member.domain.Member;

import java.time.LocalDateTime;

@Data
public class SignUpResponseDto {
    private String username;
    private String email;
    private LocalDateTime createAt;

    public SignUpResponseDto(Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.createAt = member.getCreatedAt();
    }
}
