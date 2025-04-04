package kr.co.greendae.controller;

import kr.co.greendae.dto.support.LectureDTO;
import kr.co.greendae.dto.support.RecordDTO;
import kr.co.greendae.dto.support.RegisterDTO;

import kr.co.greendae.dto.support.StudentDTO;
import kr.co.greendae.dto.support.pageRegister.PageRequestDTO;
import kr.co.greendae.dto.support.pageRegister.PageResponseDTO;
import kr.co.greendae.dto.support.pageRegisterList.RegisteredPageRequestDTO;
import kr.co.greendae.dto.support.pageRegisterList.RegisteredPageResponseDTO;
import kr.co.greendae.entity.Lecture.Lecture;
import kr.co.greendae.service.SupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 학생 지원
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/support")
public class SupportController {

    private final SupportService supportService;

    //교과과정
    @GetMapping("/classes")
    public String classes(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        // 학번
        String stdNo = userDetails.getUsername();
        StudentDTO studentDTO = supportService.findStudentByStdNo(stdNo);

        // 학년, 학과

        // 전공 데이터 출력
        // 전공 1학년 데이터 출력
        List<LectureDTO> majorListLevel1 = supportService.findLectureByLecCate(studentDTO, "1");

        // 전공 2학년 데이터 출력
        List<LectureDTO> majorListLevel2 = supportService.findLectureByLecCate(studentDTO, "2");

        // 전공 3학년 데이터 출력
        List<LectureDTO> majorListLevel3 = supportService.findLectureByLecCate(studentDTO, "3");

        // 전공 4학년 데이터 출력
        List<LectureDTO> majorListLevel4 = supportService.findLectureByLecCate(studentDTO, "4");

        // 교양 데이터 출력
        List<LectureDTO> generalList = supportService.findLectureByLecClass(studentDTO, "교양");

        model.addAttribute("majorListLevel1", majorListLevel1);
        model.addAttribute("majorListLevel2", majorListLevel2);
        model.addAttribute("majorListLevel3", majorListLevel3);
        model.addAttribute("majorListLevel4", majorListLevel4);
        model.addAttribute("generalList", generalList);

        // 데이터 필터링 예시 (lecGrade별로 그룹화)
        Map<Integer, List<LectureDTO>> groupedGeneralList = generalList.stream()
                .collect(Collectors.groupingBy(LectureDTO::getLecGrade));

        model.addAttribute("groupedGeneralList", groupedGeneralList);

        return "/support/classes";
    }

    //성적
    @GetMapping("/grade")
    public String gradeByStdNo(@AuthenticationPrincipal UserDetails userDetails, RegisteredPageRequestDTO registeredPageRequestDTO, Model model){
        //학번
        String stdNo = userDetails.getUsername();

        RegisteredPageResponseDTO registeredPageResponseDTO = supportService.findGradeByStdNo(registeredPageRequestDTO, stdNo);
        int total = supportService.totalCredit(registeredPageResponseDTO);

        model.addAttribute("gradeDTOList", registeredPageResponseDTO.getDtoList());
        model.addAttribute("pageResponseDTO", registeredPageResponseDTO);
        model.addAttribute("total", total);

        //List<RegisterDTO> gradeList = supportService.findGradeByStdNo(stdNo);
        //model.addAttribute("gradeList", gradeList);

        return "/support/grade";
    }

    //수강신청
    @GetMapping("/register")
    public String register(@AuthenticationPrincipal UserDetails userDetails, PageRequestDTO pageRequestDTO, Model model){

        //학번조회
        String stdNo = userDetails.getUsername();
        log.info("stdNo: " + stdNo);

        // 학생의 학년 조회
        int stdYear = supportService.findStudentYearByStdNo(stdNo);
        log.info("stdYear: " + stdYear);

        //글 조회 서비스 호출
        PageResponseDTO pageResponseDTO = supportService.findRegisterByStdNoByGrade(pageRequestDTO, stdYear);

        model.addAttribute("lectureDTOList", pageResponseDTO.getDtoList());
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "/support/register";
    }

    //수강신청 버튼 활성화
    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<?> registerLecture(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RegisterDTO registerDTO) {

        //학번조회
        String stdNo = userDetails.getUsername();
        String lecNo = registerDTO.getRegLecNo();

        StudentDTO student = supportService.findStudentByStdNo(stdNo);
        Lecture lecture = supportService.findLectureByLecNo(lecNo);

        // 총원 초과 체크
        if(lecture.getLecStdTotal() == lecture.getLecStdCount()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "capacity_exceeded"));
        }

        // 동일한 강의를 신챙했는가
        Boolean isDuplicate = supportService.CheckRegister(student, lecture);
        if(isDuplicate){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "duplicate_registration"));
        }

        log.info("stdNo: " + stdNo);

        boolean success = supportService.registerLecture(student,lecture);


        if(success){
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "unknown_error"));
        }
    }

    //수강신청 검색하기
    @GetMapping("/search_register")
    public String search(@AuthenticationPrincipal UserDetails userDetails, Model model, PageRequestDTO pageRequestDTO){

        //학번조회
        String stdNo = userDetails.getUsername();

        // 학생의 학년 조회
        int stdYear = supportService.findStudentYearByStdNo(stdNo);

        //서비스 호출
        PageResponseDTO pageResponseDTO = supportService.searchAll(pageRequestDTO, stdYear);

        model.addAttribute("lectureDTOList", pageResponseDTO.getDtoList());
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "/support/register";
    }

    //내역
    @GetMapping("/register_list")
    public String registerListByStdNo(@AuthenticationPrincipal UserDetails userDetails, RegisteredPageRequestDTO registeredPageRequestDTO, Model model){
        //학번 조회
        String stdNo = userDetails.getUsername();
        log.info("stdNo: " + stdNo);

        //글 조회 서비스
        RegisteredPageResponseDTO registeredPageResponseDTO = supportService.findRegisterByStdNo(registeredPageRequestDTO, stdNo);

        int total = supportService.totalCredit(registeredPageResponseDTO);

        model.addAttribute("registeredDTOList", registeredPageResponseDTO.getDtoList());
        model.addAttribute("pageResponseDTO", registeredPageResponseDTO);
        model.addAttribute("total", total);

        return "/support/register_list";
    }


    //내역 취소 활성화
    @ResponseBody
    @PostMapping("/cancel_lecture")
    public ResponseEntity<Map<String, Object>> cancelLecture(@RequestParam("lecNo") String lecNo) {

        System.out.println(lecNo);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isCancelled = supportService.cancelLecture(lecNo);
            response.put("success", isCancelled);
            if (isCancelled) {
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "수강취소에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("message", "예상치 못한 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    //학적
    @GetMapping("/record")
    public String recordByStdNo(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String stdNo = userDetails.getUsername();
        log.info("stdNo: " + stdNo);

        List<StudentDTO> studentList = supportService.findRecordByStdNo(stdNo);
        List<String> lectureYears = supportService.getLectureYearsByStudent(stdNo);
        System.out.println("YEAR@@@@" + lectureYears);

        if (!studentList.isEmpty()) {
            StudentDTO studentDTO = studentList.get(0);  // 첫 번째 학생의 정보 가져오기
            String studentStdNo = studentDTO.getStdNo();  // 학번 가져오기
            log.info("Student StdNo: " + studentStdNo);  // 학번 출력 (확인용)

            // 학점 계산
            SupportService.CreditSummary creditSummary = supportService.calculateCredits(stdNo);

            double averageCredits = 0;
            int totalCredits = creditSummary.getMajor() + creditSummary.getElective() + creditSummary.getOther();
            if(totalCredits > 0){
                averageCredits = (double) totalCredits / 3;
            }

            // 년도/ 학기별 취득학점현환
            // 학번 202510010
            // 2025년 1학기 2학기


            List<RecordDTO> recordDTOS = supportService.calculateRecode(studentDTO);

            // 모델에 학생 정보와 학점 요약 추가
            model.addAttribute("student", studentDTO);
            model.addAttribute("creditSummary", creditSummary);
            model.addAttribute("averageCredits", averageCredits);
            model.addAttribute("recordDTOS", recordDTOS);
        } else {
            log.warn("No student found for stdNo: " + stdNo);  // 학생 정보가 없을 경우 경고 로그
        }

        // 결과적으로 학적 정보를 보여주는 페이지로 포워드
        return "/support/record";
    }

}