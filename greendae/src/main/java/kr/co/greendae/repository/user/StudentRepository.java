package kr.co.greendae.repository.user;

import kr.co.greendae.dto.support.StudentDTO;
import kr.co.greendae.dto.user.UserDTO;
import kr.co.greendae.entity.user.Student;
import kr.co.greendae.repository.user.custom.StudentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String>, StudentRepositoryCustom {

    //학번, 이름, 학과, 휴대폰번호, 주민번호, 이메일, 학년/학기, 재학상태
    @Query("select (s.stdNo, s.stdYear, s.stdSemester, s.stdClass, s.stdStatus, u.name, u.hp, u.email, u.ssn, s.registerCredits) from User as u " +
            "join Student as s on u.uid = s.user.uid " +
            "where s.stdNo = :stdNo")
    public List<Object[]> findRecordByStdNo(@Param("stdNo") String stdNo);

    //학번으로 학년 조회
    @Query("SELECT s.stdYear FROM Student s WHERE s.stdNo = :stdNo")
    public int findYearByStdNo(String stdNo);

    public Student findByStdNo(String regLecNo);
}
