package proejct.predix.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proejct.predix.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
