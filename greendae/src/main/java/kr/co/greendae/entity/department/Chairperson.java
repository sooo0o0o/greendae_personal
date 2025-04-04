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
@Table(name = "Chairperson")
public class Chairperson {
    // 학과장 테이블

    @Id
    private int no;
    private String name;
    

}
