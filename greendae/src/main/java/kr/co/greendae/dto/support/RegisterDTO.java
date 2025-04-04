package kr.co.greendae.dto.support;

import kr.co.greendae.entity.Lecture.Lecture;
import kr.co.greendae.entity.user.Professor;
import kr.co.greendae.entity.user.Student;
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

    private String regAttenScore;
    private String regEtcScore;
    private String regFinalScore;
    private String regMidScore;
    private int regTotalScore;   //점수
    private String regGradeScore; //A,B,C,D,E
    private String regStdNo;    //과목 수강하는 학생 번호
    private String regLecNo;    //과목 코드
    //LecNo -> LecCate, LecPro, LecRoom, LecTime, LecName, LecGrade

    // 추가 필드
    private String regYear;
    private String regSemester;

    // 추가된 Lecture 관련 필드
    private Lecture lecture;
    private String lecName; //(Lecture테이블이랑 Join 한다고 추가해놓은것) 과목 명
    private String lecCate; //과목 구분
    private int lecGrade;   //수강 학년
    private String lecProName;  //담당 교수
    private String lecRoom; //강의실
    private String lecWeekday; //강의 날짜
    private int lecCredit;
    private Professor professor;
    private Student student;


}
