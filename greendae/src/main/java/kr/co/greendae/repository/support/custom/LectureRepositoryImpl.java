package kr.co.greendae.repository.support.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.greendae.dto.support.pageRegister.PageRequestDTO;
import kr.co.greendae.entity.Lecture.QLecture;
import kr.co.greendae.entity.Lecture.QRegister;
import kr.co.greendae.entity.user.QProfessor;
import kr.co.greendae.entity.user.QStudent;
import kr.co.greendae.entity.user.QUser;
import kr.co.greendae.repository.support.impl.LectureRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class LectureRepositoryImpl implements LectureRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QLecture qLecture = QLecture.lecture;
    private QUser qUser = QUser.user;
    private QStudent qStudent = QStudent.student;
    private QRegister qRegister = QRegister.register;
    private QProfessor qProfessor = QProfessor.professor;


    @Override
    public Page<Tuple> selectAllByStdNoAndStdYear(Pageable pageable, int stdYear) {

        BooleanExpression expression = qLecture.lecGrade.eq(stdYear);

        List<Tuple> tupleList = queryFactory
                .select(qLecture, qUser.name)
                .from(qLecture)
                .join(qProfessor)
                .on(qLecture.professor.proNo.eq(qProfessor.proNo))
                .join(qUser)
                .on(qUser.uid.eq(qProfessor.user.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qLecture.lecNo.desc())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                .select(qLecture.count())
                .from(qLecture).where(expression)
                .fetchOne())
                .orElse(0L); // null이면 0L 반환

        //페이징 처리를 위한 페이지 객체 기반
        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> searchLecturesByStdNoAndStdYear(PageRequestDTO pageRequestDTO, Pageable pageable, int stdYear) {
        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        //검색조건
        BooleanExpression expression = null;
        BooleanExpression year = qLecture.lecGrade.eq(stdYear);

        if(searchType.equals("lecClass")){
            expression = qLecture.lecClass.contains(keyword).and(year);
        }else if(searchType.equals("lecCate")){
            expression = qLecture.lecCate.contains(keyword).and(year);
        }else if(searchType.equals("lecNo")){
            expression = qLecture.lecNo.contains(keyword).and(year);

        }else if(searchType.equals("lecName")){
            expression = qLecture.lecName.contains(keyword).and(year);

        }else if(searchType.equals("professor")){
            expression = qLecture.professor.user.name.contains(keyword).and(year);
        }

        List<Tuple> tupleList = queryFactory
                .select(qLecture, qUser.name)
                .from(qLecture)
                .join(qProfessor)
                .on(qLecture.professor.proNo.eq(qProfessor.proNo))
                .join(qUser)
                .on(qUser.uid.eq(qProfessor.user.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qLecture.lecNo.desc())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(qLecture.count())
                        .from(qLecture)
                        .join(qProfessor)
                        .on(qLecture.professor.proNo.eq(qProfessor.proNo))
                        .join(qUser)
                        .on(qUser.uid.eq(qProfessor.user.uid))
                        .where(expression)
                        .fetchOne())
                        .orElse(0L); // null이면 0L 반환

        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectAll(Pageable pageable) {
        List<Tuple> tupleList = queryFactory
                .select(qLecture, qUser.name)
                .from(qLecture)
                .join(qProfessor)
                .on(qLecture.professor.proNo.eq(qProfessor.proNo))
                .join(qUser)
                .on(qUser.uid.eq(qProfessor.user.uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qLecture.lecNo.desc())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(qLecture.count())
                        .from(qLecture)
                        .fetchOne())
                .orElse(0L); // null이면 0L 반환

        //페이징 처리를 위한 페이지 객체 기반
        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectProfessorForSearch(kr.co.greendae.dto.page.PageRequestDTO pageRequestDTO, Pageable pageable) {
        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        //검색조건
        BooleanExpression expression = null;

        if(searchType.equals("lecClass")){
            expression = qLecture.lecClass.contains(keyword);
        }else if(searchType.equals("lecCate")){
            expression = qLecture.lecCate.contains(keyword);
        }else if(searchType.equals("lecNo")){
            expression = qLecture.lecNo.contains(keyword);

        }else if(searchType.equals("lecName")){
            expression = qLecture.lecName.contains(keyword);

        }else if(searchType.equals("professor")){
            expression = qLecture.professor.user.name.contains(keyword);
        }

        List<Tuple> tupleList = queryFactory
                .select(qLecture, qUser.name)
                .from(qLecture)
                .join(qProfessor)
                .on(qLecture.professor.proNo.eq(qProfessor.proNo))
                .join(qUser)
                .on(qUser.uid.eq(qProfessor.user.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qLecture.lecNo.desc())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(qLecture.count())
                        .from(qLecture)
                        .join(qProfessor)
                        .on(qLecture.professor.proNo.eq(qProfessor.proNo))
                        .join(qUser)
                        .on(qUser.uid.eq(qProfessor.user.uid))
                        .where(expression)
                        .fetchOne())
                .orElse(0L); // null이면 0L 반환

        return new PageImpl<>(tupleList, pageable, total);
    }
}
