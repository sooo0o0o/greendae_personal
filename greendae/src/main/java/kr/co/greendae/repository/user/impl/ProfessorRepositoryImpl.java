package kr.co.greendae.repository.user.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.entity.department.QDepartment;
import kr.co.greendae.entity.user.QProfessor;
import kr.co.greendae.entity.user.QUser;
import kr.co.greendae.repository.user.custom.ProfessorRepositoryCustom;
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
public class ProfessorRepositoryImpl implements ProfessorRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser qUser = QUser.user;
    private final QProfessor qProfessor =  QProfessor.professor;
    private final QDepartment qDepartment = QDepartment.department;

    @Override
    public Page<Tuple> selectAllForList(Pageable pageable) {

        List<Tuple> tupleList = queryFactory
                .select(qProfessor, qUser.name)
                .from(qProfessor)
                .join(qUser)
                .on(qProfessor.user.uid.eq(qUser.uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProfessor.proNo.desc())
                .fetch();

        long total = queryFactory.select(qProfessor.count()).from(qProfessor).fetchOne();

        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectProfessorForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if(searchType.equals("department")){
            expression = qProfessor.department.deptName.contains(keyword);
        }else if(searchType.equals("position")){
            expression = qProfessor.position.contains(keyword);
        }else if(searchType.equals("name")){
            expression = qProfessor.user.name.contains(keyword);
        }

        List<Tuple> tupleList = queryFactory
                .select(qProfessor, qUser.name)
                .from(qProfessor)
                .join(qUser)
                .on(qProfessor.user.uid.eq(qUser.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProfessor.proNo.desc())
                .fetch();

        long total = queryFactory.select(qProfessor.count()).from(qProfessor)
                .where(expression).fetchOne();

        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }
}
