package kr.co.greendae.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.dto.community.FileDTO;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.dto.page.PageResponseDTO;
import kr.co.greendae.entity.community.article.StateArticle;
import kr.co.greendae.service.ArticleService;
import kr.co.greendae.service.CommentService;
import kr.co.greendae.service.FileService;
import kr.co.greendae.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// 입학안내
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admission")
public class AdmissionController {

    private final HttpServletRequest request;
    private final ArticleService articleService;
    private final FileService fileService;
    private final CommentService commentService;
    private final QnaService qnaService;

    //수시
    @GetMapping("/early")
    public String early(){
        return "/admission/early";
    }

    //정시
    @GetMapping("/regular")
    public String regular(){
        return "/admission/regular";
    }

    //편입학
    @GetMapping("/transfer")
    public String transfer(){
        return "/admission/transfer";
    }

    // ad 공지사항 검색
    @GetMapping("/adnotice/search")
    public String adnoticesearch(PageRequestDTO pageRequestDTO, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "adnotice");

        // 카테고리
        String cate = (String) session.getAttribute("cate");
        log.info("pageRequestDTO:{}", pageRequestDTO);

        //서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isSearching", true);

        return "/admission/adnotice";
    }

    // ad공지사항 리스트
    @GetMapping("/adnotice")
    public String adnotice(Model model, PageRequestDTO pageRequestDTO) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "adnotice");

        // 카테고리
        String cate = (String) session.getAttribute("cate");

        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isListing", true);

        return "/admission/adnotice";
    }

    // ad공지사항 글쓰기 페이지
    @GetMapping("/adnotice/write")
    public String adnoticewrite(Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "adnotice");

        model.addAttribute("isWriting", true);
        return "/admission/adnotice";
    }

    // ad공지사항 글 등록
    @PostMapping("/adnotice/write")
    public String adnoticewrite(ArticleDTO articleDTO) {
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


        return "redirect:/admission/adnotice";  // 글쓰기 후 리스트 페이지로 리디렉션
    }

    // 노티스 글 보기 view
    @GetMapping("/adnotice/view")
    public String adnoticeview(Model model, int no){

        // 글 조회 서비스 호출
        ArticleDTO articleDTO = articleService.findById(no);

        // 파일 조회 서비스 호출
        // List로 파일 정보를 들고와서
        // no 기반으로 file 테이블 검색해서 List 가져오기
        List<FileDTO> fileDTOList = fileService.findById(no);
        articleDTO.setFiles(fileDTOList);

        model.addAttribute(articleDTO);
        model.addAttribute("isViewing", true);


        return "/admission/adnotice";
    }

    // 노티스 게시글 삭제
    @GetMapping("/adnotice/delete")
    public String adnoticedelete(int no){

        fileService.deletebasicFile(no);
        commentService.deletebasicAllComment(no);
        articleService.deletebasicArticle(no);
        return "redirect:/admission/adnotice";
    }


    // 노티스 게시글 수정
    @GetMapping("/adnotice/modify")
    public String adnoticemodify(int no, Model model) {

        // 수정 데이터 조회 서비스
        ArticleDTO articleDTO = articleService.findById(no);
        //모델 참조
        model.addAttribute("isModifying", true);
        model.addAttribute(articleDTO);

        return "/admission/adnotice";
    }

    @PostMapping("/adnotice/modify")
    public String adnoticemodify(ArticleDTO articleDTO) {
        //서비스 호출
        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트
        return "redirect:/admission/adnotice";
    }

    //상담
    @GetMapping("/consult")
    public String consult(){
        return "/admission/consult";
    }

    //
    @GetMapping("/qna")
    public String qna(Model model, PageRequestDTO pageRequestDTO){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "admission");

        String cate = "admission";
        PageResponseDTO pageResponseDTO = qnaService.findAll(pageRequestDTO, cate);
        model.addAttribute(pageResponseDTO);

        return "/admission/qna";
    }

    @GetMapping("/qna/write")
    public String qnaList(){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "admission");

        return "/admission/qna_write";
    }

    @PostMapping("/qna/write")
    public String qnaWrite(ArticleDTO articleDTO){

        HttpSession session = request.getSession();
        String cate = (String) session.getAttribute("cate");

        String regip = request.getRemoteAddr();
        articleDTO.setCate(cate);
        articleDTO.setRegip(regip);

        qnaService.register(articleDTO);

        return "redirect:/admission/qna";
    }

    @Transactional
    @GetMapping("/qna/delete")
    public String qnaDelete(int no){
        qnaService.delete(no);
        return "redirect:/admission/qna";
    }

    @GetMapping("/qna/view")
    public String qnaView(Model model, int no){

        ArticleDTO articleDTO = qnaService.findById(no);
        System.out.println(articleDTO);

        model.addAttribute("articleDTO", articleDTO);

        return "/admission/qna_view";

    }

    @PostMapping("/qna/resWrite")
    public String  qnaResWrite(ArticleDTO articleDTO){
        System.out.println(articleDTO);
        String regip = request.getRemoteAddr();
        articleDTO.setRegip(regip);
        System.out.println(articleDTO);
        StateArticle stateArticle = qnaService.registerRes(articleDTO);

        // qnaService.saveRes(stateArticle, articleDTO);

        return "redirect:/admission/qna_view";
    }

    @GetMapping("/qna/modify")
    public String qnaModify(int no, Model model){

        HttpSession session = request.getSession();
        String cate = (String) session.getAttribute("cate");

        ArticleDTO articleDTO = qnaService.findById(no);
        model.addAttribute("articleDTO", articleDTO);
        return "/admission/qna_modify";
    }

    @PostMapping("/qna/modify")
    public String  qnaModify(ArticleDTO articleDTO){

        System.out.println(articleDTO);
        qnaService.modifyQna(articleDTO);

        //return "redirect:/admission/qna_view" + articleDTO.getNo();
        return "redirect:/admission/qna";
    }

    //

}
