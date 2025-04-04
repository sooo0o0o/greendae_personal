package kr.co.greendae.entity.Lecture;


import jakarta.persistence.*;
import kr.co.greendae.entity.user.Professor;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Lecture")
public class Lecture {

    /*
    * 강의 목록 출력
    * 과목코드, 학과, 학년, 구분, 과목명, 교수, 학점, 수강시간, 강의실, 최대수강인원
    *
    * 강의 등록 시 추가 정보
    * 수업기간, 평가 양식, 교재
    * */

    @Id
    private String lecNo;   //과목 코드

    /*
    * ----------------------------------------------------
    * private String lecClass;    //학과
    * ----------------------------------------------------
    * 학과 : professor 객체
    *         - department 객체
    *               - deptName(학과 이름)
    * ----------------------------------------------------
    * */

    @ManyToOne
    @JoinColumn(name = "proNo")
    private Professor professor;  //담당교수

    private String lecMajor;
    private String lecClass;

    private String lecName;  //과목명
    private String lecCate;  //과목 구분(전공선택)
    private int lecGrade;    //학년
    private int lecCredit;   //학점
    private int lecStdTotal; //총 수강인원(추가)
    private int lecStdCount; //현재 수강인원
    private int lecLevel;    // 1,2,3,4 레벨
    private String lecRoom;  //강의실

    private String lecWeekday; // 요일 
    private String lecScheduleStart; // 수강기간(mm/dd/yy) 시작
    private String lecScheduleEnd; // 수강기간(mm/dd/yy) 종료
    private String lecTimeStart; // 강의 시작시간
    private String lecTimeEnd;   // 강의 종료시간
    
    private String book;     //교재(출판사 - 도서명 - 저자 입력)

    private String lecContent;      //강의설명
    private String lecEvaluation ;  //평가방식


}
