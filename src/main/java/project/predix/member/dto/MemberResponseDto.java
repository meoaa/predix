package project.predix.member.dto;

import lombok.Data;
import project.predix.member.domain.Member;
import project.predix.store.domain.entity.Store;

@Data
public class MemberResponseDto {
    private Long id;
    private String username;
    private Store store;

    private MemberResponseDto(Long id, String username, Store store) {
        this.id = id;
        this.username = username;
        this.store = store;
    }

    public static MemberResponseDto of(Member member){
        return new MemberResponseDto(
                member.getId(),
                member.getUsername(),
                member.getStore());

    }
}
