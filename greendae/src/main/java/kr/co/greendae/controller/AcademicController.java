package kr.co.greendae.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.dto.community.FileDTO;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.dto.page.PageResponseDTO;
import kr.co.greendae.service.ArticleService;
import kr.co.greendae.service.CommentService;
import kr.co.greendae.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// 학사안내
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/academic")
public class AcademicController {

    private final HttpServletRequest request;
    private final ArticleService articleService;
    private final FileService fileService;
    private final CommentService commentService;

    // 학사일정
    @GetMapping("/schedule")
    public String schedule(){
        return "/academic/schedule";
    }

    // 수강신청
    @GetMapping("/registration")
    public String registration(){
        return "/academic/registration";
    }

    // 수료 및 졸업
    @GetMapping("/graduation")
    public String graduation(){
        return "/academic/graduation";
    }

    // 성적
    @GetMapping("/grade")
    public String score(){
        return "/academic/grade";
    }

    // aca 공지사항 검색
    @GetMapping("/acanotice/search")
    public String acanoticesearch(PageRequestDTO pageRequestDTO, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "acanotice");

        // 카테고리
        String cate = (String) session.getAttribute("cate");
        log.info("pageRequestDTO:{}", pageRequestDTO);

        //서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isSearching", true);

        return "/academic/acanotice";
    }

    // aca공지사항 리스트
    @GetMapping("/acanotice")
    public String acanotice(Model model, PageRequestDTO pageRequestDTO) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "acanotice");

        // 카테고리
        String cate = (String) session.getAttribute("cate");

        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isListing", true);  // isWriting=false로 설정하여 리스트 화면 표시

        return "/academic/acanotice";
    }

    // aca공지사항 글쓰기 페이지
    @GetMapping("/acanotice/write")
    public String acanoticewrite(Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "acanotice");

        model.addAttribute("isWriting", true);  // isWriting=true로 설정하여 글쓰기 화면 표시
        return "/academic/acanotice";
    }

    // aca공지사항 글 등록
    @PostMapping("/acanotice/write")
    public String acanoticewrite(ArticleDTO articleDTO) {
        HttpSession session = request.getSession();
        String cate = (String) session.getAttribute("cate");

        String regip = request.getRemoteAddr();
        articleDTO.setCate(cate);
        articleDTO.setRegip(regip);
        log.info("articleDTO: {}", articleDTO);

        //파일 업로드 서비스 호출
        List<FileDTO> files = fileService.uploadFile(articleDTO);

        // 글 저장 서비스 호출
        articleDTO.setFile(files.size());
        int no = articleService.basicRegister(articleDTO);

        log.info("no: {}", no);
        log.info("files: {}", files);

        // 파일 저장 서비스 호출
        for(FileDTO fileDTO : files) {
            fileDTO.setAno(no);
            fileService.save(fileDTO);
        }


        return "redirect:/academic/acanotice";  // 글쓰기 후 리스트 페이지로 리디렉션
    }

    // aca노티스 글 보기 view
    @GetMapping("/acanotice/view")
    public String acanoticeview(Model model, int no){

        System.out.println(no);
        // 글 조회 서비스 호출
        ArticleDTO articleDTO = articleService.findById(no);

        // 파일 조회 서비스 호출
        // List로 파일 정보를 들고와서
        // no 기반으로 file 테이블 검색해서 List 가져오기
        List<FileDTO> fileDTOList = fileService.findById(no);
        articleDTO.setFiles(fileDTOList);

        model.addAttribute(articleDTO);
        model.addAttribute("isViewing", true);

        // return "redirect:/community/freeboard";
        return "/academic/acanotice";
    }

    // 노티스 게시글 삭제
    @GetMapping("/acanotice/delete")
    public String acanoticedelete(int no){

        fileService.deletebasicFile(no);
        commentService.deletebasicAllComment(no);
        articleService.deletebasicArticle(no);
        return "redirect:/academic/acanotice";
    }


    // 노티스 게시글 수정
    @GetMapping("/acanotice/modify")
    public String acanoticemodify(int no, Model model) {

        // 수정 데이터 조회 서비스
        ArticleDTO articleDTO = articleService.findById(no);
        //모델 참조
        model.addAttribute("isModifying", true);
        model.addAttribute(articleDTO);

        return "/academic/acanotice";
    }

    @PostMapping("/acanotice/modify")
    public String acanoticemodify(ArticleDTO articleDTO) {
        //서비스 호출

        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트
        return "redirect:/academic/acanotice";
    }

    // 자주묻는질문
    @GetMapping("/faq")
    public String faq(){
        return "/academic/faq";
    }
}
