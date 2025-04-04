package kr.co.greendae.entity.user;

import jakarta.persistence.*;
import kr.co.greendae.entity.Lecture.Register;
import kr.co.greendae.entity.department.Department;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Student")
public class Student {

    @Id
    private String stdNo;           //학번

    @OneToOne
    @JoinColumn(name = "uid")
    private User user;

    private String admission_year;  //입학년도
    private String graduation_year; //졸업년도
    private String admission_type;  //입학구분(수시, 정시)
    private int stdYear;            //학년
    private String stdSemester;     //학기

    private int registerCredits;    //총점

    @ManyToOne
    @JoinColumn(name = "deptNo")
    private Department department;

    // private String major;           //전공 예)(인문사회대학)
    private String stdClass;        //학과 예)(영어영문학과)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodNo")
    private Professor professor;         //지도교수
    private String stdStatus;       //재학상태

    @PrePersist
    public void prePersist(){
        if(this.stdStatus == null){
            this.stdStatus = "재학중";
        }

    }

}
