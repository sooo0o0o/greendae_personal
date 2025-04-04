package kr.co.greendae.repository.community.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.entity.community.article.QBasicArticle;
import kr.co.greendae.entity.user.QUser;
import kr.co.greendae.repository.community.custom.BasicArticleRepositoryCustom;
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
public class BasicArticleRepositoryImpl implements BasicArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QBasicArticle qArticle = QBasicArticle.basicArticle;
    private QUser qUser = QUser.user;


    @Override
    public Page<Tuple> selectAllForList(Pageable pageable, String cate) {

        BooleanExpression expression = qArticle.cate.eq(cate);

        if(cate.equals("news")) {
            BooleanExpression Column = qArticle.cate.eq("column");
            expression = expression.or(Column);
        }

        List<Tuple> tupleList = queryFactory
                .select(qArticle, qUser.name)
                .from(qArticle)
                .join(qUser)
                .on(qArticle.user.uid.eq(qUser.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qArticle.no.desc())
                .fetch();

        long total = queryFactory.select(qArticle.count()).from(qArticle).where(expression).fetchOne();


        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectAllForSearch(PageRequestDTO pageRequestDTO, Pageable pageable, String cate) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression expression = qArticle.cate.eq(cate);


        // 검색 조건에 따라 where 조건 표현식 생성
        BooleanExpression cateExpression = qArticle.cate.eq(cate);



        /*
        전체 기능 검색이 안됨
        if(searchType.equals("title")){
            expression = qArticle.title.contains(keyword);
        }else if(searchType.equals("content")){
            expression = qArticle.content.contains(keyword);
        }else if(searchType.equals("writer")){
            expression = qUser.name.contains(keyword);
        }
         */

        if (keyword != null && !keyword.trim().isEmpty()) {
            BooleanExpression titleCondition = qArticle.title.contains(keyword);
            BooleanExpression contentCondition = qArticle.content.contains(keyword);
            BooleanExpression writerCondition = qUser.name.contains(keyword);

            if ("title".equals(searchType)) {
                expression = titleCondition;
            } else if ("content".equals(searchType)) {
                expression = contentCondition;
            } else if ("writer".equals(searchType)) {
                expression = writerCondition;
            } else {
                // 전체 검색 조건 (title, content, writer 중 하나라도 포함되면 검색)
                expression = titleCondition.or(contentCondition).or(writerCondition);
            }
        }

        if(cate.equals("news")) {
            BooleanExpression Column = qArticle.cate.eq("column");
            expression = expression.and(cateExpression).or(Column);
        }else{
            expression = expression.and(cateExpression);
        }

        List<Tuple> tupleList = queryFactory
                .select(qArticle, qUser.name)
                .from(qArticle)
                .join(qUser)
                .on(qArticle.user.uid.eq(qUser.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qArticle.no.desc())
                .fetch();

        long total = queryFactory
                .select(qArticle.count())
                .from(qArticle)
                .join(qUser)
                .on(qArticle.user.uid.eq(qUser.uid))
                .where(expression)
                .fetchOne();


        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);


    }
}
