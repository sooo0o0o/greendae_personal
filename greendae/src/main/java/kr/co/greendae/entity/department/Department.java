package kr.co.greendae.entity.department;

import jakarta.persistence.*;
import kr.co.greendae.entity.college.College;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Department")
public class Department {
    // 부서 테이블

    @Id
    private int deptNo;            // 학과번호

    /*
    *   college : 대학 정보
    *
    *   name    : 대학 이름
    *   engName : 대학 영어 이름
    *   title   : 소개 제목
    *   content : 소개 내용
    * */

    private String college;

    private String deptName;       // 학과명
    private String deptEname;      // 영문명
    private String establishedYear; // 설립연도
    private String deptChair;      // 학과장
    private String deptHp;         // 학과 연락처
    private String deptOffice;     // 학과 사무실

    private int totalStudents;     // 소속 학생 수
    private int totalLecturers;   // 소속 강의 수
    private int totalProfessors;    // 소속 교수 수

}
