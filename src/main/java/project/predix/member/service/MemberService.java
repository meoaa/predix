package project.predix.member.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.auth.dto.SignUpRequestDto;
import project.predix.auth.dto.SignUpResponseDto;
import project.predix.member.domain.Member;
import project.predix.member.domain.Role;
import project.predix.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponseDto create(SignUpRequestDto dto){
        if(memberRepository.existsByUsername(dto.getUsername())){
            throw new IllegalStateException("이미 사용 중인 ID");
        }

        Member member = new Member(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getEmail(),
                dto.getNickname(),
                Role.ROLE_USER);

        Member savedMember = memberRepository.save(member);
        return new SignUpResponseDto(savedMember);
    }

}
