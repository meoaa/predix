package project.predix.member.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.auth.dto.SignUpRequestDto;
import project.predix.auth.dto.SignUpResponseDto;
import project.predix.exception.DuplicateEmailException;
import project.predix.exception.DuplicateUsernameException;
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
            throw new DuplicateUsernameException(dto.getUsername());
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

    public String checkUsername(String username){
        if(memberRepository.existsByUsername(username)){
            throw new DuplicateUsernameException();
        }
        return username;
    }
    public String checkDuplicateEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new DuplicateEmailException();
        }
        return email;
    }

}
