package kr.co.greendae.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Terms")
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Lob // Text로 저장
    private String terms;

    @Lob
    private String privacy;
}
