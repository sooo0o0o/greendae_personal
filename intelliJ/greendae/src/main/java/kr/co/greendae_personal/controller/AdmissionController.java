package kr.co.greendae_personal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//입학안내

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admission")
public class AdmissionController {


    public String notice(){
        return "notice";
    }

    @GetMapping("/early")
    public String early(){

        return "/admission/early";
    }

    public String regular(){
        return "regular";
    }

    public String transfer(){
        return "transfer";
    }

    public String consult(){
        return "consult";
    }


}
