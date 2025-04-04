package kr.co.greendae.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
*  버전 테스트용 컨트롤러 삭제 예정
* */

@RestController
public class VersionController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version}")
    private String version;

    @GetMapping("/version")
    public String index(){
        return appName + ": " + version;
    }

}
