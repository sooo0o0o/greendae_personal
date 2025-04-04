package kr.co.greendae.repository.department.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.greendae.dto.department.DepartmentDTO;
import kr.co.greendae.dto.department.PageDepartmentRequestDTO;
import kr.co.greendae.entity.department.QDepartment;
import kr.co.greendae.repository.department.custom.DepartmentRepositoryCustom;
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
public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QDepartment qDepartment = QDepartment.department;

    @Override
    public Page<Tuple> selectAllForList(Pageable pageable) {

        List<Tuple> tupleList = queryFactory
                .select(qDepartment, qDepartment)
                .from(qDepartment)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDepartment.deptNo.desc())
                .fetch();

        long total = queryFactory.select(qDepartment.count()).from(qDepartment).fetchOne();

        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectDepartmentForSearch(PageDepartmentRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if(searchType.equals("college")){
            expression = qDepartment.college.contains(keyword);
        }else if(searchType.equals("chair")){
            expression = qDepartment.deptChair.contains(keyword);
        }else if(searchType.equals("department")){
            expression = qDepartment.deptName.contains(keyword);
        }

        List<Tuple> tupleList = queryFactory
                .select(qDepartment, qDepartment)
                .from(qDepartment)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDepartment.deptNo.desc())
                .fetch();

        long total = queryFactory
                .select(qDepartment.count())
                .from(qDepartment)
                .where(expression)
                .fetchOne();


        // 페이징 처리를 위한 페이지 객체 반환
        return new PageImpl<Tuple>(tupleList, pageable, total);

    }


}
