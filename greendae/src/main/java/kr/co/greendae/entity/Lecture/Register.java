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


    /*
    * 고민1 : A,B,C,D,E,F
    * 컬럼을 2개로 한다. -> 조회할 때 따로 비즈니스 로직을 구현 안해도됨.
    * -> (편함) 하지만  A,B 와 학점은 서로 연관관계. 하나만 있어도 문제 없음.
    * 예 ) 4.0 -> A, 3.0 -> B
    * 데이터베이스 모델링 실습2 -> 평점(A,B,C,D)
    * https://drive.google.com/drive/folders/1Vow0jVg5fh3ROGwa9qywhuCgWfA1MFPy
    * */

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
