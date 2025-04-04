package kr.co.greendae.entity.event;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private int price;
    private String menuRice;
    private String menuSoup;
    private String sub1;
    private String sub2;
    private String sub3;
    private String sub4;
    private String title; // 조식, 중식, 석식
    private String date;
    private String week;
}
