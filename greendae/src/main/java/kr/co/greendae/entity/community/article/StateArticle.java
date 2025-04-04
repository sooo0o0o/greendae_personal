package kr.co.greendae.entity.community.article;

import jakarta.persistence.*;
import kr.co.greendae.entity.user.Student;
import kr.co.greendae.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "StateArticle")
public class StateArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Column(nullable = false)
    private String cate;
    private String title;
    private String content;
    private String regip;
    private String state;

    private String pass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User user;

    @CreationTimestamp
    private LocalDateTime wdate;

    @OneToOne(mappedBy = "stateArticle") // mappedBy 정확한 필드명 지정
    private ResStateArticle resStateArticle;

    @PrePersist
    public void prePersist() {
        // 엔티티 기본 속성 값 초기화
        if(this.cate == null) {
            this.cate = "qna";
        }

        if(this.state == null) {
            this.state = "답변대기";
        }

        if(this.pass == null || this.pass.equals("")) {
            this.pass = "공개";
        }
    }

}
