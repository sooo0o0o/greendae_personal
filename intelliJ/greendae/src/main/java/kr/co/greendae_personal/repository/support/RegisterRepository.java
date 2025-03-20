package kr.co.greendae_personal.repository.support;

import kr.co.greendae_personal.dto.support.LectureDTO;
import kr.co.greendae_personal.entity.Lecture.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface RegisterRepository extends JpaRepository<Register, String> {


    @Query("SELECT (r.regLecName, r.regLecProName, r.regTotalScore) " +
            "FROM Register r " +
            "WHERE r.student.stdNo = :stdNo")
    List<LectureDTO> findGradesByStudentNo(@Param("stdNo") String stdNo);

    List<Register> findByStudent_StdNo(String stdNo);
}
