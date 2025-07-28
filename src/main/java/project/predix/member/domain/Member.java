package project.predix.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.predix.member.dto.ProfileUpdateRequestDto;
import project.predix.store.domain.entity.Store;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Table(name = "tb_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Store store;

    public Member(String username, String password, String email, String nickname, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public void updateMember(ProfileUpdateRequestDto dto){
        this.nickname = dto.getNickname();
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
