package kr.co.greendae_personal.dto.support;

import kr.co.greendae_personal.entity.Lecture.Lecture;
import kr.co.greendae_personal.entity.Lecture.Register;
import kr.co.greendae_personal.entity.Lecture.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {

    private String regNo;       // 수강 번호? Auto_increment
    private String regStdNo;    // 수강하는 학생 학번 = stdNo
    private String regLecNo;    // 수강된 과목코드 = lecNo
    private String regLecName;  // 수강된 과목명 = lecName
    private String regLecProName;// 수강된 과목 담당교수 = lecProName
    private String regLecCate;  // 수강된 과목 구분(전공/교양..)
    private int regTotalScore;  // 과목 점수
    private String regGrade;    // 등급 A,B,C,D,F
    private int regCredit;      // 학점 (F 면 0점?)

    public Register toEntity(Student student, Lecture lecture){
        return Register.builder()
                .regNo(regNo)
                .student(student)
                .lecture(lecture)
                .regLecName(regLecName)
                .regLecProName(regLecProName)
                .regLecCate(regLecCate)
                .regTotalScore(regTotalScore)
                .regGrade(regGrade)
                .regCredit(regCredit)
                .build();
    }

}
