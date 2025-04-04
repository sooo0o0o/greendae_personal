package kr.co.greendae.dto.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDTO {

    // 년도	학년	학기	신청학점	전공학점	선택학점	기타학점	총취득학점	평점평균
    private String year;     //년도
    private int grade;    //학년
    private String semester; //학기
    private int register; //신청학점
    private int major;    //전공학점
    private int elective; //선택학점
    private int etc;      //기타학점
    private int total;    //총취득학점
    private String average;  //평점평균
    
}
