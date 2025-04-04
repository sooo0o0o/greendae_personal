package kr.co.greendae.entity.Lecture;

import jakarta.persistence.*;
import kr.co.greendae.entity.user.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Register")
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regNo;

    @ManyToOne
    @JoinColumn(name = "regStdNo", referencedColumnName = "stdNo")
    private Student student;

    /*
    *   Lecture (외례키 : LecNo)
    *   -------------------------------------------------
    *   lecCate  : 과목구분
    *   professor - user - name : 교수 이름
    *   lecRoom  : 강의실
    *   lecName  : 과목명
    *   lecGrade : 학년
    *   l
    * */

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "regLecNo", referencedColumnName = "lecNo")
    private Lecture lecture;

    private String regYear;
    private String regSemester;

    private String regGradeScore; //A,B,C,D,E
    private String regAttenScore; // 출석점수
    private String regMidScore;   // 중간고수 점수
    private String regFinalScore; // 기말고수 점수
    private String regEtcScore;   // 기타점수
    private int regTotalScore;    // 총점

    @PrePersist
    public void prePersist(){
        if(this.regAttenScore == null){
            this.regAttenScore = "0";
        }
        if(this.regMidScore == null){
            this.regMidScore = "0";
        }
        if(this.regFinalScore == null){
            this.regFinalScore = "0";
        }
        if(this.regEtcScore == null){
            this.regEtcScore = "0";
        }
    }



}
