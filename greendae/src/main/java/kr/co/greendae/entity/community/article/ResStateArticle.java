package kr.co.greendae.entity.community.article;

import jakarta.persistence.*;
import kr.co.greendae.entity.user.Student;
import kr.co.greendae.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "ResStateArticle")
public class ResStateArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String regip;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User user;

    @OneToOne
    @JoinColumn(name = "ano") // 외래 키 컬럼 명 지정
    private StateArticle stateArticle;

    @CreationTimestamp
    private LocalDateTime wdate;


}
