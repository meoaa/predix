package project.predix.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.predix.store.domain.Store;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "tb_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Store store;

    public Member(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
    }

    /*  연관관계 메서드 */
    public void assignStore(Store store){
        if(this.store != null && this.store.getMember() == this){
            this.store.changeMember(null);
        }
        this.store = store;

        if(store != null && this.store.getMember() != this){
            this.store.changeMember(this);
        }
    }
}
