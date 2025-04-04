package kr.co.greendae.repository.support;

import kr.co.greendae.entity.Lecture.Lecture;
import kr.co.greendae.entity.Lecture.Register;
import kr.co.greendae.entity.user.Student;
import kr.co.greendae.repository.support.impl.RegisterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface RegisterRepository extends JpaRepository<Register, Integer> , RegisterRepositoryCustom {
    //Register_list 페이지에 출력할 정보 JOIN

    @Query("select (r.student.stdNo, r.lecture.lecNo, l.lecCredit, l.lecName, l.lecCate, l.lecGrade, l.professor, l.lecRoom, l.lecWeekday) from Register as r " +
            "join Lecture as l on r.lecture.lecNo = l.lecNo " +
            "where r.student.stdNo = :stdNo")
    public List<Object[]> findRegisterByStdNo(String stdNo);

    //Grade 페이지에 출력할 정보 JOIN
    @Query("select (r.student.stdNo, r.lecture.lecNo, r.regTotalScore, r.regGradeScore, l.lecCredit, l.lecName, l.lecCate, l.lecGrade, l.professor) " +
            "from Register as r " +
            "join Lecture as l on r.lecture.lecNo = l.lecNo " +
            "where r.student.stdNo = :stdNo")
    public List<Object[]> findGradeByStdNo(String stdNo);


    void deleteByLecture(Lecture lecture);

    Register findByLecture(Lecture lecture);

    List<Register> findByStudent(Student student);

    List<Register> findByStudentAndRegSemesterAndRegYear(Student student, String year, String semester);

    Boolean existsByStudentAndLecture(Student student, Lecture lecture);
}