package kr.co.greendae_personal.entity.Lecture;


import jakarta.persistence.*;
import kr.co.greendae_personal.dto.support.LectureDTO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Lecture")
public class Lecture {

    @Id
    private int lecNo;

    private String lecName;
    private String lecCate;
    private int lecGrade;
    private String lecProName;
    private int lecCredit;
    private int lecStdNo;
    private String lecClass;
    private String lecRoom;
    private String lecTime;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Register> registers;

    public LectureDTO toDTO(){
        return LectureDTO.builder()
                .lecNo(lecNo)
                .lecName(lecName)
                .lecCate(lecCate)
                .lecGrade(lecGrade)
                .lecProName(lecProName)
                .lecCredit(lecCredit)
                .lecStdNo(lecStdNo)
                .lecClass(lecClass)
                .lecRoom(lecRoom)
                .lecTime(lecTime)
                .build();
    }


}
