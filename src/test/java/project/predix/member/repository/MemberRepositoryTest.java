package project.predix.member.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.predix.member.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("멤버 생성 테스트")
    public void createMemberTest(){
        Member member = new Member("username",
                "password",
                "email@email.com",
                "nickname");

        Member savedMember = memberRepository.save(member);

        em.flush();
        em.clear();

        assertThat(savedMember.getId()).isEqualTo(member.getId());
        assertThat(savedMember).isEqualTo(member);
    }

    @Test
    @DisplayName("멤버 생성 후 조회 테스트")
    public void createMemberAndFindTest(){
        Member member = new Member("username",
                "password",
                "email@email.com",
                "nickname");

        Member savedMember = memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("전체 멤버 조회 테스트")
    public void findAllMembers(){
        Member member1 = new Member("username1",
                "password1",
                "email1@email.com",
                "nickname");

        Member member2 = new Member("username2",
                "password2",
                "email2@email.com",
                "nickname2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> findAllMembers = memberRepository.findAll();
        long count = memberRepository.count();
        assertThat(findAllMembers.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    public void deleteMember(){
        Member member1 = new Member("username1",
                "password1",
                "email1@email.com",
                "nickname");

        Member member2 = new Member("username2",
                "password2",
                "email2@email.com",
                "nickname2");

        Member savedMember = memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.delete(savedMember);

        em.flush();
        em.clear();

        List<Member> findAllMembers = memberRepository.findAll();
        assertThat(findAllMembers.size()).isEqualTo(1);
    }
}