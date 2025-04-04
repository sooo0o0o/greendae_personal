package kr.co.greendae.dto.event;

import jakarta.persistence.*;
import kr.co.greendae.entity.event.Gallery;
import kr.co.greendae.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GalleryDTO {

    private int no;
    private String title;      //제목
    private String content;    //내용 
    private String comment;    //댓글
    private String imageoName; //이미지 기존 이름
    private String imagesName; //이미지 변환 이름
    private int hit;           //조회수
    private String regip;
    private User user;
    private String wdate;


}
