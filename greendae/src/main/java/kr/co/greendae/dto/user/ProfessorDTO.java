package kr.co.greendae.dto.user;

import kr.co.greendae.dto.department.DepartmentDTO;
import kr.co.greendae.entity.department.Department;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProfessorDTO {

    private String uid;               //아이디(fk)
    private String proNo;             //교수번호(pk)
    private String graduationSchool;  //졸업대학
    private String fieldOfStudy;      //학문분야
    private String graduationDate;    //졸업일
    private int deptNo;
    private String degree;            //학위
    private String departmentC;        //담당학과(대학)
    private String departmentD;        //담당학과(학과)
    private String appointmentDate;   // 임용일

    private DepartmentDTO department;

    // 재직정보
    private String status;

    // 직위 (정교수, 조교수, 부교수)
    private String position;

    private UserDTO user;

    // 재직정보? (재직중, 휴직, 퇴직)

}
