package kr.co.greendae.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 대학 소개
@Controller
@RequestMapping("/intro")
public class IntroController {

    // 총장 인사말
    @GetMapping("/greetings")
    public String greetings(){
        return "/intro/greetings";
    }

    // 연혁
    @GetMapping("/history")
    public String history(){
        return "/intro/history";
    }

    // 오시는길
    @GetMapping("/location")
    public String location(){
        return "/intro/location";
    }

    // 조직도
    @GetMapping("/organizationchart")
    public String organizationchart(){
        return "/intro/organizationchart";
    }

    // 교육이념
    @GetMapping("/Philosophy")
    public String philosophy(){
        return "/intro/Philosophy";
    }

}
