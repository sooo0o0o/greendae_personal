package kr.co.greendae.dto.community;


import kr.co.greendae.dto.user.UserDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ArticleDTO {

    private int no;
    private String cate;    // Employ_Article에는 없음
    private String title;
    private String content;
    private int comment;
    private int file;
    private int hit;
    private String writer;
    private String regip;
    private String wdate;
    private String pass;

    // Employ_Article 내용 추가
    private String edate;

    // State_Article 내용 추가
    private String state;

    // 추가 필드
    private UserDTO user;
    private List<FileDTO> files;
    private String nick;


    public String getWdate() {
        if(wdate != null){
            return wdate.substring(0, 10);
        }
        return null;
    }



    // 첨부파일 객체
    private MultipartFile file1;
    private MultipartFile file2;

    public List<MultipartFile> getMultipartFiles(){
        return List.of(file1, file2);
    }



}
