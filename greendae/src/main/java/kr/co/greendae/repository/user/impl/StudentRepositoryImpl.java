package kr.co.greendae.repository.user.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.entity.user.QStudent;
import kr.co.greendae.entity.user.QUser;
import kr.co.greendae.repository.user.custom.StudentRepositoryCustom;
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
public class StudentRepositoryImpl implements StudentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser qUser = QUser.user;
    private final QStudent qStudent =  QStudent.student;

    @Override
    public Page<Tuple> selectAllForList(Pageable pageable) {

        List<Tuple> tupleList = queryFactory
                .select(qStudent, qUser.name)
                .from(qStudent)
                .join(qUser)
                .on(qStudent.user.uid.eq(qUser.uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qStudent.stdNo.desc())
                .fetch();

        long total = queryFactory.select(qStudent.count()).from(qStudent).fetchOne();

        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);

    }

    @Override
    public Page<Tuple> selectStudentForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if(searchType.equals("department")){
            expression = qStudent.stdClass.contains(keyword);
        }else if(searchType.equals("stdYear")){
            expression = qStudent.stdYear.eq(Integer.parseInt(keyword));
        }else if(searchType.equals("name")){
            expression = qStudent.user.name.contains(keyword);
        }

        List<Tuple> tupleList = queryFactory
                .select(qStudent, qUser.name)
                .from(qStudent)
                .join(qUser)
                .on(qStudent.user.uid.eq(qUser.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qStudent.stdNo.desc())
                .fetch();

        long total = queryFactory.select(qStudent.count()).from(qStudent)
                .where(expression).fetchOne();

        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);

    }


}
