package kr.co.greendae.entity.community.file;

import jakarta.persistence.*;
import kr.co.greendae.entity.community.article.BasicArticle;
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
@Table(name = "BasicFile")
public class BasicFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fno;

    private int ano;
    private String oName;
    private String sName;
    private int download;

    @CreationTimestamp
    private LocalDateTime rdate;


}
