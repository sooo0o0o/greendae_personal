package kr.co.greendae.entity.college;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.greendae.entity.department.Department;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "College")
public class College {

    @Id
    private String name;       // 대학 이름
    private String engName;    // 대학 영어 이름
    private String title;      // 소개 제목
    private String content;    // 소개 내용
    private String oName;      // 이미지 기존 이름
    private String sName;      // 이미지 변경 이름

    @OneToMany(mappedBy = "college") // mappedBy 속성은 매핑되는 엔티티의 FK 컬럼
    private List<Department> departments;

}
