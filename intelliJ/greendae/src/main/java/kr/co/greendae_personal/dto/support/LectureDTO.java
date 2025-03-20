package kr.co.greendae_personal.dto.support;

import kr.co.greendae_personal.entity.Lecture.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureDTO {

    private int lecNo;      // 과목코드
    private String lecName; // 과목명
    private String lecCate; // 구분(전공/교양)
    private int lecGrade;   // 수강학년
    private String lecProName;//담당교수
    private int lecCredit;  // 학점
    private int lecStdNo;   // 수강인원
    private String lecClass;// 개설 학과
    private String lecRoom; // 강의실
    private String lecTime; // 강의 날짜

    public Lecture toEntity(){
        return Lecture.builder()
                .lecNo(lecNo)
                .lecName(lecName)
                .lecCate(lecCate)
                .lecGrade(lecGrade)
                .lecProName(lecProName)
                .lecCredit(lecCredit)
                .lecStdNo(lecStdNo)
                .lecClass(lecClass)
                .lecRoom(lecRoom)
                .lecTime(lecTime)
                .build();
    }

}
