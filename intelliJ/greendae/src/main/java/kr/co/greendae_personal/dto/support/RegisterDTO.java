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

    private int regNo;          //auto_increase(PK용으로 필요해서 만든것) 과목 번호

    private String regStdNo;    //과목 수강하는 학생 번호
    private String regLecNo;    //과목 코드
    //LecNo -> LecCate, LecPro, LecRoom, LecTime, LecName, LecGrade
    private int regTotalScore;   //점수
    private String regGradeScore; //A,B,C,D,E
    private int regCredit;  //학점
    private String regYear;
    private String regSemester;

    // 추가된 Lecture 관련 필드
    private String lecName; //(Lecture테이블이랑 Join 한다고 추가해놓은것) 과목 명
    private String lecCate; //과목 구분
    private int lecGrade;   //수강 학년
    private String lecProName;  //담당 교수
    private String lecRoom; //강의실
    private String lecTime; //강의 날짜

    public Register toRegisterEntity(Student student, Lecture lecture) {
        return Register.builder()
                .regNo(regNo)
                .student(student)
                .lecture(lecture)
                .regTotalScore(regTotalScore)
                .regGradeScore(regGradeScore)
                .regCredit(regCredit)
                .regYear(regYear)
                .regSemester(regSemester)
                .build();
    }

    /*
    private int regNo;       // 수강 번호? Auto_increment
    private String regStdNo;    // 수강하는 학생 학번 = stdNo
    private String regLecNo;    // 수강된 과목코드 = lecNo
    private String regLecName;  // 수강된 과목명 = lecName
    private String regLecProName;// 수강된 과목 담당교수 = lecProName
    private String regLecCate;  // 수강된 과목 구분(전공/교양..)
    private int regTotalScore;  // 과목 점수
    private String regGrade;    // 등급 A,B,C,D,F
    private int regCredit;      // 학점 (F 면 0점?)
    private String regLecRoom;
    private String regLecTime;

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
                .regLecRoom(regLecRoom)
                .regLecTime(regLecTime)
                .build();
    }

     */

}
