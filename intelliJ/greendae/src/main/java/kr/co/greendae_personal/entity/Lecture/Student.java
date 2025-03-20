package kr.co.greendae_personal.entity.Lecture;

import jakarta.persistence.*;
import kr.co.greendae_personal.dto.support.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Student")
public class Student {
    @Id
    private String stdNo;

    private String stdName;
    private String stdHp;
    private int stdYear;
    private String stdAddress;
    private String stdSemester;
    private String stdClass;
    private String stdEmail;
    private String stdSSN;
    private String stdStatus;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Register> registers;

    public StudentDTO toDTO(){
        return StudentDTO.builder()
                .stdNo(stdNo)
                .stdName(stdName)
                .stdHp(stdHp)
                .stdYear(stdYear)
                .stdAddress(stdAddress)
                .stdSemester(stdSemester)
                .stdClass(stdClass)
                .stdEmail(stdEmail)
                .stdSSN(stdSSN)
                .stdStatus(stdStatus)
                .build();
    }
}
