package kr.co.greendae_personal.controller;

import kr.co.greendae_personal.dto.support.LectureDTO;
import kr.co.greendae_personal.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 학생 지원
@Controller
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private final SupportService supportService;

    @GetMapping("/classes")
    public String classes(){
        return "/support/classes";
    }

    @GetMapping("/grade")
    public String grade(){
        return "/support/grade";
    }

    @GetMapping("/record")
    public String record(){
        return "/support/record";
    }

    @GetMapping("/register")
    public String register(Model model){

        List<LectureDTO> lectureDTOList = supportService.findAll();
        model.addAttribute("lectureDTOList", lectureDTOList);

        return "/support/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Map<String, String>> registerLecture(@RequestBody Map<String, String > requestData){
        String lecNo = requestData.get("lecNo");
        System.out.println("수강 신청 : " + lecNo);

        //수강신청 로직 추가하기
        Map<String, String> response = new HashMap<>();
        response.put("message", lecNo + "수강 신청 완료!");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/register_list")
    public String register_list(Model model){
        List<LectureDTO> registeredLectures = supportService.getRegisteredLectures();
        model.addAttribute("registeredLectures", registeredLectures);

        return "/support/register_list";
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, String>> cancelLecture(@RequestBody Map<String, String> requestData) {
        String lecNo = requestData.get("lecNo");
        System.out.println("수강 취소: " + lecNo);

        // 실제 DB에서 해당 강의를 취소하는 로직 추가
        boolean success = supportService.delete(lecNo);

        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", lecNo + " 강의 취소 완료!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "강의 취소 실패!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/grade/{stdNo}")
    public String grade(@PathVariable String stdNo, Model model){
        List<LectureDTO> gradeList = supportService.getGradeByStudent(stdNo);
        model.addAttribute("gradeList", gradeList);

        return "/support/grade";
    }

    @GetMapping("/register_list/{stdNo}")
    public String getRegisteredLecturesByStudent(@PathVariable String stdNo, Model model) {
        // SupportService에서 학생의 수강 과목 조회
        List<LectureDTO> registeredLectures = supportService.getRegisteredLecturesByStudent(stdNo);

        // 조회한 수강 과목 리스트를 모델에 추가
        model.addAttribute("registeredLectures", registeredLectures);

        // 수강 과목 정보를 보여줄 뷰로 이동
        return "/support/register_list";
    }
}
