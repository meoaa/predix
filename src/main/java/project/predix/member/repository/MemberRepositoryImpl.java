package project.predix.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.predix.member.domain.Member;
import project.predix.member.domain.QMember;
import project.predix.store.domain.entity.QStore;

import java.util.Optional;

import static project.predix.member.domain.QMember.*;
import static project.predix.store.domain.entity.QStore.*;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findByIdWithStore(Long id) {

        QMember qMember = member;
        QStore qStore = store;
        Member result = queryFactory
                .select(qMember)
                .leftJoin(qMember.store, qStore).fetchJoin()
                .where(qMember.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
