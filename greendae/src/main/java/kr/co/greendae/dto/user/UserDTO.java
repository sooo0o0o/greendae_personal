package kr.co.greendae.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO{

    private String uid;     // 아이디
    private String pass;    // 비밀번호 (기초값 : 주민)
    private String name;    // 이름
    private String email;   // 이메일
    private String hp;      // 전화번호
    private String role;    // 역할(관리자, 학생, 교사)
    private String regip;   // IP주소
    private String zip;     // 우편번호
    private String addr1;   // 주소
    private String addr2;   // 자세한 주소
    private String ssn;     // 주민번호
    private String ename;   // 영문명
    private String gender;  // 성
    private String nationality; // 국적
    private LocalDateTime regDate; // 가입일자
    private LocalDateTime leaveDate; //탈퇴일자

}