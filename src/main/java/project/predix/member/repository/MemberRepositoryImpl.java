package project.predix.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.predix.member.domain.Member;

import java.util.Optional;

import static project.predix.member.domain.QMember.*;
import static project.predix.store.domain.entity.QStore.*;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findByIdWithStore(Long id) {

        Member result = queryFactory
                .selectFrom(member)
                .leftJoin(member.store, store).fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
