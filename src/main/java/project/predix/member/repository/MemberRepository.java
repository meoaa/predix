package project.predix.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Member> findByUsername(String username);
}
