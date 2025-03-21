package kr.co.greendae_personal.entity.Lecture;


import jakarta.persistence.*;
import kr.co.greendae_personal.dto.support.LectureDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Lecture")
public class Lecture {

    @Id
    @Column(name = "lecNo")
    private String lecNo;   //과목 코드

    private String lecName; //과목명
    private String lecCate; //과목 구분
    private int lecGrade;   //학년
    private String lecProName;  //담당교수
    private int lecCredit;  //학점
    private int lecStdCount;    //수강인원
    private String lecClass;    //학과
    private String lecRoom; //강의실
    private String lecTime; //강의날짜

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Register> registers = new ArrayList<>();

    public LectureDTO toLectureDTO(){
        return LectureDTO.builder()
                .lecNo(lecNo)
                .lecName(lecName)
                .lecCate(lecCate)
                .lecGrade(lecGrade)
                .lecProName(lecProName)
                .lecCredit(lecCredit)
                .lecStdCount(lecStdCount)
                .lecClass(lecClass)
                .lecRoom(lecRoom)
                .lecTime(lecTime)
                .build();
    }


    /*
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

     */


}
