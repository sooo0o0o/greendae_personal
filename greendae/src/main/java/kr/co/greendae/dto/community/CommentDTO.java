package kr.co.greendae.dto.community;

import kr.co.greendae.dto.user.UserDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CommentDTO {

    private int cno;
    private int parent;
    private String content;
    private String writer;
    private String regip;
    private String wdate;

    private UserDTO user;

    public String getWdate() {
        if(wdate != null){
            return wdate.substring(0,10);
        }
        return null;
    }


}
