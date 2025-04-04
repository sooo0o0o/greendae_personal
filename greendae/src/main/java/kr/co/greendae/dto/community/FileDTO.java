package kr.co.greendae.dto.community;

import lombok.*;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FileDTO {
    private int fno;
    private int ano;
    private String oName;
    private String sName;
    private int download;
    private String rdate;

    // 추가 필드
    private String contentType;
    private Resource resource;


}
