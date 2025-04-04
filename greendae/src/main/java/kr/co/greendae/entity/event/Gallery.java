package kr.co.greendae.entity.event;

import jakarta.persistence.*;
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
@Table(name = "Gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private String title;      //제목
    private String content;    //내용
    private String comment;
    private String imageoName; //이미지 기존 이름
    private String imagesName; //이미지 변환 이름
    private int hit;           //조회수
    private String regip;      // 등록 아이피


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User user; // 작성자

    @CreationTimestamp
    private LocalDateTime wdate; // 작성날짜

}
