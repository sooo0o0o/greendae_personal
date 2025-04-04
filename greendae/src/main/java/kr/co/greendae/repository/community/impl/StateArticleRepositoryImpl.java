package kr.co.greendae.repository.community.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.entity.community.article.QBasicArticle;
import kr.co.greendae.entity.community.article.QStateArticle;
import kr.co.greendae.entity.user.QUser;
import kr.co.greendae.repository.community.custom.BasicArticleRepositoryCustom;
import kr.co.greendae.repository.community.custom.StateArticleRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StateArticleRepositoryImpl implements StateArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QStateArticle qStateArticle = QStateArticle.stateArticle;
    private QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllForList(Pageable pageable, String cate) {

        BooleanExpression expression = qStateArticle.cate.eq(cate);

        List<Tuple> tupleList = queryFactory
                .select(qStateArticle, qUser.name)
                .from(qStateArticle)
                .join(qUser)
                .on(qStateArticle.user.uid.eq(qUser.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qStateArticle.no.desc())
                .fetch();

        long total = queryFactory.select(qStateArticle.count()).from(qStateArticle).where(expression).fetchOne();


        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }
}
