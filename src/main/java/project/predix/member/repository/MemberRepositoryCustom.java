package project.predix.member.repository;

import project.predix.member.domain.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findByIdWithStore(Long id);
}
