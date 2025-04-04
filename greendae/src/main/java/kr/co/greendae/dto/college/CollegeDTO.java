package kr.co.greendae.dto.college;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CollegeDTO {

    private String name;       // 대학 이름
    private String engName;    // 대학 영어 이름
    private String title;      // 소개 제목
    private String content;    // 소개 내용
    
    // 이미지를 위한 필드1
    private MultipartFile image;
    private String oName;
    private String sName;
    

}
