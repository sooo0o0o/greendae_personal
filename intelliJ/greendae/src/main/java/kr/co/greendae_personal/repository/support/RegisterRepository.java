package kr.co.greendae_personal.repository.support;

import kr.co.greendae_personal.dto.support.LectureDTO;
import kr.co.greendae_personal.entity.Lecture.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface RegisterRepository extends JpaRepository<Register, String> {

    //Register_list 페이지에 출력할 정보 JOIN
    @Query("select (r.student.stdNo, r.lecture.lecNo, r.regCredit, l.lecName, l.lecCate, l.lecGrade, l.lecProName, l.lecRoom, l.lecTime) from Register as r " +
            "join Lecture as l on r.lecture.lecNo = l.lecNo " +
            "where r.student.stdNo = :stdNo")
    public List<Object[]> findRegisterByStdNo(@Param("stdNo") String stdNo);

    //Grade 페이지에 출력할 정보 JOIN
    @Query("select (r.student.stdNo, r.lecture.lecNo, r.regTotalScore, r.regGradeScore, r.regCredit, l.lecName, l.lecCate, l.lecGrade, l.lecProName) from Register as r " +
            "join Lecture as l on r.lecture.lecNo = l.lecNo " +
            "where r.student.stdNo = :stdNo")
    public List<Object[]> findGradeByStdNo(@Param("stdNo") String stdNo);









    /*
    @Query("SELECT (r.student.stdNo, r.lecture.lecNo, r.regLecName, r.regLecProName, r.regGrade, r.regCredit, r.regLecCate, r.regTotalScore) " +
            "FROM Register r " +
            "WHERE r.student.stdNo = :stdNo")
    List<LectureDTO> findGradesByStudentNo(@Param("stdNo") String stdNo);

    public List<Register> findRegisterByStd(String stdNo);

    @Query("select (r.student.stdNo, r.lecture.lecNo, r.regLecName, r.regLecProName, r.regGrade, r.regCredit, r.regLecCate, r.regLecRoom, r.regLecTime) " +
            "from Register r " +
            "where r.student.stdNo = :stdNo")
    public List<Object[]> findRegisterByStdNo(@Param("stdNo") String stdNo);

     */
}
