package kr.co.greendae.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.greendae.dto.college.CollegeDTO;
import kr.co.greendae.dto.department.*;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.dto.support.LectureDTO;
import kr.co.greendae.dto.support.RegisterDTO;
import kr.co.greendae.dto.support.StudentDTO;
import kr.co.greendae.dto.support.pageRegister.PageResponseDTO;
import kr.co.greendae.dto.user.PageProfessorResponseDTO;
import kr.co.greendae.dto.user.PageStudentResponseDTO;
import kr.co.greendae.dto.user.ProfessorDTO;
import kr.co.greendae.dto.user.UserDTO;
import kr.co.greendae.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// 관리자
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    // 메인 페이지
    @GetMapping("/main")
    public String main(){
        return "/admin/main";
    }

    // 대학 및 학과 등록
    @GetMapping("/college/register")
    public String college(Model model){

        List<CollegeDTO> collegeDTOS = adminService.findAllCollege();
        List<ChairPersonDTO> chairPersonDTOS = adminService.findAllChairPerson();

        model.addAttribute("collegeDTOS",collegeDTOS);
        model.addAttribute("chairPersonDTOS",chairPersonDTOS);

        return "/admin/register/college";
    }

    @PostMapping("/college/register")
    public String college(CollegeDTO collageDTO){

        log.info("college register : collageDTO={}", collageDTO);
        // 이미지 등록 서비스 호출
        adminService.uploadImage(collageDTO);
        adminService.save(collageDTO);

        return "redirect:/admin/main";
    }

    @PostMapping("/department/register")
    public String department(DepartmentDTO departmentDTO,
                             @RequestParam("date")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        departmentDTO.setEstablishedYear(String.valueOf(date.getYear()));

        // 최신 학과번호 추출
        int deptNo = adminService.getMaxDepNo();
        departmentDTO.setDeptNo(deptNo);

        System.out.println(departmentDTO);
        System.out.println(departmentDTO);
        System.out.println(departmentDTO);

        adminService.register(departmentDTO);

        return "redirect:/admin/college/register";
    }

    // 교수 등록 페이지
    @GetMapping("/professor/register")
    public String professor(Model model){


        // Select 박스를 위한 대학 정보
        List<CollegeDTO>  collegeDTOS = adminService.findAllCollege();
        List<DepartmentDTO> departmentDTOS = adminService.findAllDepartmentByName(collegeDTOS.get(0).getName());

        //대학 정보
        model.addAttribute("collegeDTOS",collegeDTOS);
        model.addAttribute("departmentDTOS",departmentDTOS);
        return "/admin/register/professor";
    }

    // 교수 등록
    @PostMapping("/professor/register")
    public String  professor(ProfessorDTO professorDTO, UserDTO userDTO
            , @RequestParam("graduationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate graduationDate,
                             @RequestParam("appointmentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate
            , HttpServletRequest req){

        // 교수번호 : 연도 + 학과번호 + 순번 자동 조합
        String prodNo = String.valueOf(appointmentDate.getYear());


        DepartmentDTO departmentDTO = adminService.findDepartmentByName(professorDTO.getDepartmentD());
        prodNo += departmentDTO.getDeptNo();

        // 비밀번호 추출 (주민번호 뒷자리)
        String[] passStr = userDTO.getSsn().split("-");
        String pass = passwordEncoder.encode(passStr[1]); ;
        userDTO.setPass(pass);
        userDTO.setRole("Professor");
        userDTO.setRegip(req.getRemoteAddr());

        // User 테이블에 저장 - code는 교수번호
        String code = adminService.registerUser(userDTO, prodNo);

        professorDTO.setUid(code);
        professorDTO.setProNo(code);
        professorDTO.setDeptNo(departmentDTO.getDeptNo());

        adminService.registerProfessor(professorDTO, userDTO, departmentDTO);

        // 학과에 교수 수 + 1 하기
        adminService.CountUpProfessor(departmentDTO);


        System.out.println(departmentDTO);
        return "redirect:/admin/professor/register";
    }

    // 학생 등록 페이지
    @GetMapping("/student/register")
    public String student(Model model){


        // Select 박스를 위한 대학 정보
        List<CollegeDTO>  collegeDTOS = adminService.findAllCollege();
        List<DepartmentDTO> departmentDTOS = adminService.findAllDepartmentByName(collegeDTOS.get(0).getName());

        //대학 정보
        model.addAttribute("collegeDTOS",collegeDTOS);
        model.addAttribute("departmentDTOS",departmentDTOS);
        return "/admin/register/student";
    }

    // 학생 등록
    @PostMapping("/student/register")
    public String  student(StudentDTO studentDTO, UserDTO userDTO
            , HttpServletRequest req){

        // 학생번호 : 연도 + 학과번호 + 순번 자동 조합
        String studNo = studentDTO.getAdmission_year();  // 연도 추가
        DepartmentDTO departmentDTO = adminService.findDepartmentByName(studentDTO.getStdClass());
        studNo += departmentDTO.getDeptNo();

        log.info("Department: " + departmentDTO);

        // 비밀번호 추출 (주민번호 뒷자리)
        String[] passStr = userDTO.getSsn().split("-");
        String pass = passwordEncoder.encode(passStr[1]); ;
        userDTO.setPass(pass);
        userDTO.setRole("Student");
        userDTO.setRegip(req.getRemoteAddr());

        // User 테이블에 저장 - code는 학생번호
        String code = adminService.registerUser(userDTO, studNo);

        studentDTO.setUid(code);
        studentDTO.setStdNo(code);
        adminService.registerStudent(studentDTO, userDTO, departmentDTO);

        adminService.CountUpStudent(departmentDTO);

        return "redirect:/admin/student/register";
    }

    // 강의 등록
    @GetMapping("/lecture/register")
    public String lecture(Model model){

        // Select 박스를 위한 대학 정보
        List<CollegeDTO>  collegeDTOS = adminService.findAllCollege();
        List<DepartmentDTO> departmentDTOS = adminService.findAllDepartmentByName(collegeDTOS.get(0).getName());

        //대학 정보
        model.addAttribute("collegeDTOS",collegeDTOS);
        model.addAttribute("departmentDTOS",departmentDTOS);

        return "/admin/register/lecture";
    }

    @PostMapping("/lecture/register")
    public String lecture(LectureDTO lectureDTO){

        // 과목 코드 만들기

        String[] year = lectureDTO.getLecScheduleStart().split("-");
        String lecNo = year[0];

        DepartmentDTO departmentDTO = adminService.findDepartmentByName(lectureDTO.getLecClass());

        lecNo += departmentDTO.getDeptNo();

        lectureDTO.setLecClass(departmentDTO.getDeptName());

        adminService.registerLecture(lectureDTO, lecNo);

        adminService.CountUpLecture(departmentDTO);

        return "redirect:/admin/lecture/register";
    }

    // 대학을 선택하면 관련 학과를 출력
    @ResponseBody
    @GetMapping("/department/list")
    public List<DepartmentDTO> departmentList(@RequestParam("college") String college){
        List<DepartmentDTO> departmentDTOS = adminService.findAllDepartmentByName(college);
        return departmentDTOS;
    }

    // 학과를 선택하면 관련 교수를 출력
    @ResponseBody
    @GetMapping("/professor/list")
    public List<ProfessorDTO> professorList(@RequestParam("department") String department){
        System.out.println("시작");
        System.out.println(department);
        System.out.println("끝");
        List<ProfessorDTO> professorDTOS = adminService.findAllProfessorByName(department);

        return professorDTOS;
    }

    /*
     * 목록
     * */

    // 학과
    @GetMapping("/departments/list")
    public String departmentList(Model model, PageDepartmentRequestDTO pageDepartmentRequestDTO){

        // 전체 학과 조회 서비스 호출(JPA)
        PageDepartmentResponseDTO pageDepartmentResponseDTO = adminService.findAllDepartment(pageDepartmentRequestDTO);
        model.addAttribute("pageResponseDTO",pageDepartmentResponseDTO);
        return "/admin/list/department";
    }

    // 학과 검색
    @GetMapping("/departments/search")
    public String search(PageDepartmentRequestDTO pageRequestDTO, Model model){
        log.info("pageRequestDTO : {}", pageRequestDTO);

        // 서비스 호출
        PageDepartmentResponseDTO pageResponseDTO = adminService.searchAllDepartment(pageRequestDTO);
        System.out.println(pageResponseDTO);

        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/departmentSearch";
    }

    // 학생 리스트 출력
    @GetMapping("/student/list")
    public String  studentList(Model model, PageRequestDTO pageRequestDTO){

        PageStudentResponseDTO pageStudentResponseDTO = adminService.findAllStudent(pageRequestDTO);
        model.addAttribute("pageResponseDTO",pageStudentResponseDTO);
        System.out.println(pageStudentResponseDTO);

        return "/admin/list/student";
    }

    // 학생 검색
    @GetMapping("/student/search")
    public String search(PageRequestDTO pageRequestDTO, Model model){
        log.info("pageRequestDTO : {}", pageRequestDTO);

        // 서비스 호출
        PageStudentResponseDTO pageResponseDTO = adminService.searchAllStudent(pageRequestDTO);
        System.out.println(pageResponseDTO);

        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/studentSearch";
    }

    // 교수 리스트 출력
    @GetMapping("/professors/list")
    public String professorList(Model model, PageRequestDTO pageRequestDTO){

        PageProfessorResponseDTO pageProfessorResponseDTO = adminService.findAllProfessor(pageRequestDTO);
        model.addAttribute("pageResponseDTO",pageProfessorResponseDTO);

        return "/admin/list/professor";
    }

    // 교수 검색
    @GetMapping("/professor/search")
    public String professorSearch(PageRequestDTO pageRequestDTO, Model model){
        log.info("pageRequestDTO : {}", pageRequestDTO);

        // 서비스 호출
        PageProfessorResponseDTO pageResponseDTO = adminService.searchAllProfessor(pageRequestDTO);
        System.out.println(pageResponseDTO);

        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/professorSearch";
    }

    // 교육 운영 현황 리스트
    @GetMapping("/operation/list")
    public String  operation(Model model, PageRequestDTO pageRequestDTO){

        PageLectureResponseDTO pageResponseDTO = adminService.allLecture(pageRequestDTO);
        System.out.println(pageResponseDTO);
        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/operation";
    }

    // 교육 운영 검색
    @GetMapping("/operation/search")
    public String operationSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO : {}", pageRequestDTO);

        // 서비스 호출
        PageLectureResponseDTO pageResponseDTO = adminService.searchAllLecture(pageRequestDTO);
        System.out.println(pageResponseDTO);

        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/operationSearch";
    }


    // 수강 현황 리스트
    @GetMapping("/lecture/list")
    public String  lectureList(Model model, PageRequestDTO pageRequestDTO){

        PageLectureResponseDTO pageResponseDTO = adminService.allLecture(pageRequestDTO);
        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/lecture";
    }

    // 수강 현황 검색
    // 교육 운영 현황 리스트
    @GetMapping("/lecture/search")
    public String lectureSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO : {}", pageRequestDTO);

        // 서비스 호출
        PageLectureResponseDTO pageResponseDTO = adminService.searchAllLecture(pageRequestDTO);
        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/lectureSearch";
    }

    // 수강 현황 리스트
    @GetMapping("/register/list")
    public String  register(Model model, PageRequestDTO pageRequestDTO){

        PageRegisterResponseDTO pageResponseDTO = adminService.allRegister(pageRequestDTO);
        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/register";
    }

    @GetMapping("/register/search")
    public String registerSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO : {}", pageRequestDTO);

        // 서비스 호출
        PageRegisterResponseDTO pageResponseDTO = adminService.searchAllRegister(pageRequestDTO);
        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "/admin/list/registerSearch";
    }



}