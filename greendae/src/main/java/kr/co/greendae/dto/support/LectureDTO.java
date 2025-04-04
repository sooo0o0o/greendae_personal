package kr.co.greendae.dto.support;

import kr.co.greendae.dto.user.ProfessorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureDTO {

    private String lecNo;
    private String proNo;

    private String lecMajor;
    private String lecClass;
    private String lecName;
    private String lecCate;
    private int lecGrade;   //학년
    private int lecCredit;
    private int lecLevel;    // 1,2,3,4 레벨
    private int lecStdTotal; //총 수강인원
    private int lecStdCount;

    private String lecRoom;

    private String lecContent;      //강의설명
    private String lecEvaluation ;  //평가방식

    private String lecScheduleStart; // 수강기간(mm/dd/yy) 시작
    private String lecScheduleEnd; // 수강기간(mm/dd/yy) 종료
    private String lecTimeStart; // 강의 시작시간
    private String lecTimeEnd;   // 강의 종료시간
    private String book;
    private String lecWeekday; // 요일
    private String proName;

    private int per; // 강의 퍼센트 (관리자 페이지 )

    private ProfessorDTO professor;



}
