package kr.co.greendae.dto.support;

import kr.co.greendae.dto.user.ProfessorDTO;
import kr.co.greendae.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private String uid;             //아이디
    private String stdNo;           //학번
    private String admission_year;  //입학년도
    private String graduation_year; //졸업년도
    private String admission_type;  //입학구분(수시, 정시)
    private int stdYear;            //학년
    private String stdSemester;     //학기
    private String stdClass;        //학과
    private String major;           //전공
    private String advisor;         //지도교수
    private String stdStatus;       //재학상태

    private UserDTO user;
    private ProfessorDTO professor;

    private int registerCredits;    //학생 신청 학점 총점;

    //추가필드

    private String name;
    private String email;
    private String hp;
    private String ssn;

}
