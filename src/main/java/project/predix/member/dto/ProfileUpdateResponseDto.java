package project.predix.member.dto;

import lombok.Data;
import project.predix.member.domain.Member;

@Data
public class ProfileUpdateResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;

    private ProfileUpdateResponseDto(Long id, String username, String email, String nickname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
    }

    public static ProfileUpdateResponseDto of(Member member){
        return new ProfileUpdateResponseDto(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getNickname());
    }
}
