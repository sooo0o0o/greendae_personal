package kr.co.greendae.dto.department;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DepartmentDTO {
    private int deptNo;            // 학과번호
    private String college;         // 단과대학
    private String deptName;       // 학과명
    private String deptEname;   // 영문명
    private String establishedYear; // 설립연도
    private String deptChair;      // 학과장
    private String deptHp;         // 학과 연락처
    private String deptOffice;     // 학과 사무실

    private int totalStudents;      // 소속 학생 수
    private int totalLecturers;     // 소속 강의 수
    private int totalProfessors;    // 소속 교수 수

}
