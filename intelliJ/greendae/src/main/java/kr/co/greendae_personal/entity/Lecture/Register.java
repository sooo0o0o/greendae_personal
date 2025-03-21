package kr.co.greendae_personal.entity.Lecture;

import jakarta.persistence.*;
import kr.co.greendae_personal.dto.support.RegisterDTO;
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

    @ManyToOne
    @JoinColumn(name = "regLecNo", referencedColumnName = "lecNo")
    private Lecture lecture;
    //LecNo -> LecCate, LecPro, LecRoom, LecTime, LecName, LecGrade

    private int regTotalScore;
    private String regGradeScore; //A,B,C,D,E
    private int regCredit;
    private String regYear;
    private String regSemester;

    public RegisterDTO toRegisterDTO(){
        return RegisterDTO.builder()
                .regNo(regNo)
                .regStdNo(student.getStdNo())
                .regLecNo(lecture.getLecNo())
                .regTotalScore(regTotalScore)
                .regGradeScore(regGradeScore)
                .regCredit(regCredit)
                .regYear(regYear)
                .regSemester(regSemester)
                //Lecture
                .lecName(lecture.getLecName())
                .lecCate(lecture.getLecCate())
                .lecGrade(lecture.getLecGrade())  // Default 0 or null 처리 가능
                .lecProName(lecture.getLecProName())
                .lecRoom(lecture.getLecRoom())
                .lecTime(lecture.getLecTime())
                .build();
    }

    /*
    @Id
    private int regNo;

    @ManyToOne
    @JoinColumn(name = "stdNo")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lecNo")
    private Lecture lecture;
    private String regLecName;
    private String regLecProName;
    private String regLecCate;
    private int regTotalScore;
    private String regGrade;
    private int regCredit;
    private String regLecRoom;
    private String regLecTime;


    public RegisterDTO toDTO(){
        return RegisterDTO.builder()
                .regNo(regNo)
                .regStdNo(String.valueOf(student))
                .regLecNo(String.valueOf(lecture))
                .regLecName(regLecName)
                .regLecProName(regLecProName)
                .regLecCate(regLecCate)
                .regTotalScore(regTotalScore)
                .regGrade(regGrade)
                .regCredit(regCredit)
                .regLecRoom(regLecRoom)
                .regLecTime(regLecTime)
                .build();
    }

     */


}
