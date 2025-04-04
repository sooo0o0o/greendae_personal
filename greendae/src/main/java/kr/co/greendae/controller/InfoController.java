package kr.co.greendae.controller;

import kr.co.greendae.dto.college.CollegeDTO;
import kr.co.greendae.dto.department.DepartmentDTO;
import kr.co.greendae.service.CollegeService;
import kr.co.greendae.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// 대학·대학원
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/info")
public class InfoController {

    private final DepartmentService departmentService;
    private final CollegeService collegeService;
    // 인문사회대학
    @GetMapping("/social")
    public String social(Model model){

        String college  = "인문사회대학";
        CollegeDTO collegeDTO = collegeService.findByName(college);
        List<DepartmentDTO> departmentDTOS = departmentService.findDepartmentByCollege(college);

        model.addAttribute("college",collegeDTO);
        model.addAttribute("departmentDTOS", departmentDTOS);
        return "/info/social";
    }

    // 자연과학대학
    @GetMapping("/natural")
    public String natural(Model model){

        String college = "자연과학대학";
        CollegeDTO collegeDTO = collegeService.findByName(college);
        List<DepartmentDTO> departmentDTOS = departmentService.findDepartmentByCollege(college);

        model.addAttribute("college",collegeDTO);
        model.addAttribute("departmentDTOS", departmentDTOS);
        return "/info/natural";
    }

    // 공과대학
    @GetMapping("/engineering")
    public String engineering(Model model){
        
        String collage = "공과대학";
        CollegeDTO collegeDTO = collegeService.findByName(collage);
        List<DepartmentDTO> departmentDTOS = departmentService.findDepartmentByCollege(collage);

        model.addAttribute("college",collegeDTO);
        model.addAttribute("departmentDTOS", departmentDTOS);

        return "/info/engineering";
    }

    // 사범대학
    @GetMapping("/education")
    public String education(Model model){

        String collage = "사범대학";
        CollegeDTO collegeDTO = collegeService.findByName(collage);
        List<DepartmentDTO> departmentDTOS = departmentService.findDepartmentByCollege(collage);

        model.addAttribute("college",collegeDTO);
        model.addAttribute("departmentDTOS", departmentDTOS);



        return "/info/education";
    }

    // 대학원
    @GetMapping("/graduate")
    public String graduate(Model model){

        String collage = "대학원";
        CollegeDTO collegeDTO = collegeService.findByName(collage);
        List<DepartmentDTO> departmentDTOS = departmentService.findDepartmentByCollege(collage);

        model.addAttribute("college",collegeDTO);
        model.addAttribute("departmentDTOS", departmentDTOS);
        return "/info/graduate";
    }
}
