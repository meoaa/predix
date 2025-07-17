package project.predix.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.predix.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
