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
    private String regNo;

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
                .build();
    }


}
