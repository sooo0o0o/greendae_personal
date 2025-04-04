package kr.co.greendae.entity.user;

import jakarta.persistence.*;
import kr.co.greendae.entity.department.Department;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Professor")
public class Professor {

    @Id
    private String proNo;             //교수번호

    @OneToOne
    @JoinColumn(name = "uid")
    private User user;               //유저

    /*
    * Department
    * : deptName : 학과 이름 : 컴퓨터 공학과
    * : university  : 대학 객체
    *     - university - name : 대학명 : 인문사회대학
    * */

    @ManyToOne
    @JoinColumn(name = "deptNo")
    private Department department;

    // private String fieldOfStudy;      //학문분야
    // private String department;        //담당학과

    private String appointmentDate;   // 임용일
    private String graduationDate;    //졸업일

    private String graduationSchool;  //졸업대학
    private String degree;            //학위

    // 재직정보
    private String status;

    // 직위 (정교수, 조교수, 부교수)
    private String position;

    @PrePersist
    public void prePersist(){
        if(this.status == null){
            this.status = "재직중";
        }

        if(this.position == null){
            this.position = "정교수";
        }
    }


}
