package kr.co.greendae_personal.dto.support;

import kr.co.greendae_personal.entity.Lecture.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private String stdNo;       //학번
    private String stdName;     //이름
    private String stdHp;       //전화번호
    private int stdYear;        //학년
    private String stdAddress;  //주소
    private String stdSemester; //학기
    private String stdClass;    //학과
    private String stdEmail;    //이메일
    private String stdSSN;      //주민번호
    private String stdStatus;   //재학상태

    public Student toEntity() {
        return Student.builder()
                .stdNo(stdNo)
                .stdName(stdName)
                .stdHp(stdHp)
                .stdYear(stdYear)
                .stdAddress(stdAddress)
                .stdSemester(stdSemester)
                .stdClass(stdClass)
                .stdEmail(stdEmail)
                .stdSSN(stdSSN)
                .stdStatus(stdStatus)
                .build();
    }


}
