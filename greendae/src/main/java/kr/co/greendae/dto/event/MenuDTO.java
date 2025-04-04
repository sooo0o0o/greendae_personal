package kr.co.greendae.dto.event;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuDTO {
    private int no;
    private int price;
    private String menuRice;
    private String menuSoup;
    private String sub1;
    private String sub2;
    private String sub3;
    private String sub4;
    private String date;
    private String week;
}
