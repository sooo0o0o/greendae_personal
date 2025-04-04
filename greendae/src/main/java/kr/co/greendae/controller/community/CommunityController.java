package kr.co.greendae.controller.community;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.dto.community.FileDTO;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.dto.page.PageResponseDTO;
import kr.co.greendae.entity.community.article.ResStateArticle;
import kr.co.greendae.entity.community.article.StateArticle;
import kr.co.greendae.service.ArticleService;
import kr.co.greendae.service.CommentService;
import kr.co.greendae.service.FileService;
import kr.co.greendae.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

// 커뮤니티
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {

    private final HttpServletRequest request;
    private final ArticleService articleService;
    private final FileService fileService;
    private final CommentService commentService;
    private final QnaService qnaService;


    // 공지사항 검색
    @GetMapping("/notice/search")
    public String noticesearch(PageRequestDTO pageRequestDTO, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "notice");

        // 카테고리
        String cate = (String) session.getAttribute("cate");
        log.info("pageRequestDTO:{}", pageRequestDTO);

        //서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isSearching", true);

        return "/community/notice";
    }

    // 공지사항 리스트
    @GetMapping("/notice")
    public String notice(Model model, PageRequestDTO pageRequestDTO) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "notice");

        // 카테고리
        String cate = (String) session.getAttribute("cate");

        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isListing", true);  // isWriting=false로 설정하여 리스트 화면 표시

        return "/community/notice";
    }

    // 공지사항 글쓰기 페이지
    @GetMapping("/notice/write")
    public String noticewrite(Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "notice");

        model.addAttribute("isWriting", true);  // isWriting=true로 설정하여 글쓰기 화면 표시
        return "/community/notice";
    }

    // 공지사항 글 등록
    @PostMapping("/notice/write")
    public String noticewrite(ArticleDTO articleDTO) {
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


        return "redirect:/community/notice";  // 글쓰기 후 리스트 페이지로 리디렉션
    }

    // 노티스 글 보기 view
    @GetMapping("/notice/view")
    public String noticeview(Model model, int no){

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
        return "/community/notice";
    }

    // 노티스 게시글 삭제
    @GetMapping("/notice/delete")
    public String noticedelete(int no){

        fileService.deletebasicFile(no);
        commentService.deletebasicAllComment(no);
        articleService.deletebasicArticle(no);
        return "redirect:/community/notice";
    }


    // 노티스 게시글 수정
    @GetMapping("/notice/modify")
    public String noticemodify(int no, Model model) {

        // 수정 데이터 조회 서비스
        ArticleDTO articleDTO = articleService.findById(no);
        //모델 참조
        model.addAttribute("isModifying", true);
        model.addAttribute(articleDTO);

        return "/community/notice";
    }

    @PostMapping("/notice/modify")
    public String noticemodify(ArticleDTO articleDTO) {
        //서비스 호출
        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트
        return "redirect:/community/notice";
    }





    // 뉴스 검색
    @GetMapping("/news/search")
    public String newssearch(PageRequestDTO pageRequestDTO, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "news");

        // 카테고리
        String cate = (String) session.getAttribute("cate");
        log.info("pageRequestDTO:{}", pageRequestDTO);

        //서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isSearching", true);

        return "/community/news";
    }

    // 뉴스 리스트
    @GetMapping("/news")
    public String news(Model model, PageRequestDTO pageRequestDTO) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "news");


        // 카테고리
        String cate = (String) session.getAttribute("cate");

        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isListing", true);  // isWriting=false로 설정하여 리스트 화면 표시

        return "/community/news";
    }

    // 뉴스 글쓰기 페이지
    @GetMapping("/news/write")
    public String newswrite(Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "news");

        model.addAttribute("isWriting", true);  // isWriting=true로 설정하여 글쓰기 화면 표시
        return "/community/news";
    }

    // 뉴스 글 등록
    @PostMapping("/news/write")
    public String newswrite(ArticleDTO articleDTO) {
        HttpSession session = request.getSession();
        String cate = articleDTO.getCate();
        //String cate = (String) session.getAttribute("cate");

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


        return "redirect:/community/news";  // 글쓰기 후 리스트 페이지로 리디렉션
    }

    // 뉴스 글 보기 view
    @GetMapping("/news/view")
    public String newsview(Model model, int no){

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
        return "/community/news";
    }

    // 뉴스 게시글 삭제
    @GetMapping("/news/delete")
    public String newsdelete(int no){

        fileService.deletebasicFile(no);
        commentService.deletebasicAllComment(no);
        articleService.deletebasicArticle(no);
        return "redirect:/community/news";
    }


    // 뉴스 게시글 수정
    @GetMapping("/news/modify")
    public String newsmodify(int no, Model model) {

        // 수정 데이터 조회 서비스
        ArticleDTO articleDTO = articleService.findById(no);
        //모델 참조
        model.addAttribute("isModifying", true);
        model.addAttribute(articleDTO);

        return "/community/news";
    }

    // 뉴스 수정
    @PostMapping("/news/modify")
    public String newsmodify(ArticleDTO articleDTO) {
        HttpSession session = request.getSession();

        // 사용자가 변경한 카테고리 값을 가져옴
        String cate = articleDTO.getCate();

        // 변경된 카테고리를 다시 설정
        articleDTO.setCate(cate);

        // 서비스 호출
        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트 시 카테고리에 맞는 리스트 페이지로 이동
        return "redirect:/community/news";

        /*
        //서비스 호출

        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트
        return "redirect:/community/news";

         */
    }






    // 취업정보 검색
    @GetMapping("/employment/search")
    public String employmentsearch(PageRequestDTO pageRequestDTO, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "employment");

        // 카테고리
        String cate = (String) session.getAttribute("cate");
        log.info("pageRequestDTO:{}", pageRequestDTO);

        //서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isSearching", true);

        return "/community/employment";
    }



    // 취업정보 리스트
    @GetMapping("/employment")
    public String employment(Model model, PageRequestDTO pageRequestDTO) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "employment");

        // 카테고리
        String cate = (String) session.getAttribute("cate");

        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isListing", true);  // isWriting=false로 설정하여 리스트 화면 표시

        return "/community/employment";
    }



    // 취업정보 글쓰기 페이지
    @GetMapping("/employment/write")
    public String employmentwrite(Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "employment");

        model.addAttribute("isWriting", true);  // isWriting=true로 설정하여 글쓰기 화면 표시
        return "/community/employment";
    }

    // 취업정보 글 등록
    @PostMapping("/employment/write")
    public String employmentwrite(ArticleDTO articleDTO ,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate edate) {

        articleDTO.setEdate(edate.toString());
        System.out.println(articleDTO);

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


        return "redirect:/community/employment";  // 글쓰기 후 리스트 페이지로 리디렉션
    }

    // 취업정보 글 보기 view
    @GetMapping("/employment/view")
    public String employmentview(Model model, int no){

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
        return "/community/employment";
    }

    @GetMapping("/employment/delete")
    public String employmentdelete(int no){

        fileService.deletebasicFile(no);
        commentService.deletebasicAllComment(no);
        articleService.deletebasicArticle(no);
        return "redirect:/community/employment";
    }

    @GetMapping("/employment/modify")
    public String employmentmodify(int no, Model model) {

        // 수정 데이터 조회 서비스
        ArticleDTO articleDTO = articleService.findById(no);
        //모델 참조
        model.addAttribute("isModifying", true);
        model.addAttribute(articleDTO);

        return "/community/employment";
    }

    @PostMapping("/employment/modify")
    public String employmentmodify(ArticleDTO articleDTO) {
        //서비스 호출

        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트
        return "redirect:/community/employment";
    }








    // 자유게시판 검색
    @GetMapping("/freeboard/search")
    public String search(PageRequestDTO pageRequestDTO, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "freeboard");

        String cate = (String) session.getAttribute("cate");
        log.info("pageRequestDTO:{}", pageRequestDTO);

        //서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isSearching", true);

        return "/community/freeboard";
    }


    // 자유게시판 리스트 list
    @GetMapping("/freeboard")
    public String freeboard(Model model, PageRequestDTO pageRequestDTO) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "freeboard");

        String cate = (String) session.getAttribute("cate");

        //List<ArticleDTO> articleDTOList = articleService.findAllByCate("freeboard");
        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO, cate);


        //model.addAttribute("articleDTOList", articleDTOList);
        //System.out.println(articleDTOList);
        model.addAttribute(pageResponseDTO);
        model.addAttribute("isListing", true);  // isWriting=false로 설정하여 리스트 화면 표시
        return "/community/freeboard";  // freeboard.html을 렌더링
    }

    // 자유게시판 글쓰기 페이지
    @GetMapping("/freeboard/write")
    public String write(Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "freeboard");

        model.addAttribute("isWriting", true);  // isWriting=true로 설정하여 글쓰기 화면 표시
        return "/community/freeboard";  // freeboard.html을 렌더링하며 write.html 포함
    }

    // 자유게시판 글 등록
    @PostMapping("/freeboard/write")
    public String write(ArticleDTO articleDTO) {
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


        return "redirect:/community/freeboard";  // 글쓰기 후 리스트 페이지로 리디렉션
    }

    // 자유게시판 글 보기 view
    @GetMapping("/freeboard/view")
    public String view(Model model, int no){

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
        return "/community/freeboard";
    }

    @GetMapping("/freeboard/delete")
    public String delete(int no){

        fileService.deletebasicFile(no);
        commentService.deletebasicAllComment(no);
        articleService.deletebasicArticle(no);
        return "redirect:/community/freeboard";
    }

    @GetMapping("/freeboard/modify")
    public String modify(int no, Model model) {

        // 수정 데이터 조회 서비스
        ArticleDTO articleDTO = articleService.findById(no);
        //모델 참조
        model.addAttribute("isModifying", true);
        model.addAttribute(articleDTO);

        return "/community/freeboard";
    }

    @PostMapping("/freeboard/modify")
    public String modify(ArticleDTO articleDTO) {
        //서비스 호출

        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트
        return "redirect:/community/freeboard";
    }


    /* 여기서 부터 작업 */
    //질답    
    @GetMapping("/qna")
    public String qna(Model model, PageRequestDTO pageRequestDTO){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "qna");

        String cate = "qna";
        PageResponseDTO pageResponseDTO = qnaService.findAll(pageRequestDTO, cate);
        model.addAttribute(pageResponseDTO);


        return "/community/qna";
    }

    @GetMapping("/qna/write")
    public String qnaList(){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "qna");

        return "/community/qna_write";
    }

    @PostMapping("/qna/write")
    public String qnaWrite(ArticleDTO articleDTO){

        HttpSession session = request.getSession();
        String cate = (String) session.getAttribute("cate");

        String regip = request.getRemoteAddr();
        articleDTO.setCate(cate);
        articleDTO.setRegip(regip);

        qnaService.register(articleDTO);

        return "/community/qna_write";
    }

    @Transactional
    @GetMapping("/qna/delete")
    public String qnaDelete(int no){
        System.out.println("삭제시작");
        qnaService.delete(no);
        return "redirect:/community/qna";
    }

    @GetMapping("/qna/view")
    public String qnaView(Model model, int no){

        ArticleDTO articleDTO = qnaService.findById(no);
        System.out.println(articleDTO);

        model.addAttribute("articleDTO", articleDTO);

        return "/community/qna_view";

    }

    @PostMapping("/qna/resWrite")
    public String  qnaResWrite(ArticleDTO articleDTO){
        System.out.println(articleDTO);
        String regip = request.getRemoteAddr();
        articleDTO.setRegip(regip);
        System.out.println(articleDTO);
        StateArticle stateArticle = qnaService.registerRes(articleDTO);

        // qnaService.saveRes(stateArticle, articleDTO);

        return "redirect:/community/qna";
    }

    @GetMapping("/qna/modify")
    public String qnaModify(int no, Model model){

        HttpSession session = request.getSession();
        String cate = (String) session.getAttribute("cate");

        ArticleDTO articleDTO = qnaService.findById(no);
        model.addAttribute("articleDTO", articleDTO);
        return "/community/qna_modify";
    }

    @PostMapping("/qna/modify")
    public String  qnaModify(ArticleDTO articleDTO){

        System.out.println(articleDTO);
        qnaService.modifyQna(articleDTO);

        //return "redirect:/admission/qna_view" + articleDTO.getNo();
        return "redirect:/community/qna";
    }

    /*        */

    // 자료실 검색
    @GetMapping("/data/search")
    public String datasearch(PageRequestDTO pageRequestDTO, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("cate", "data");

        // 카테고리
        String cate = (String) session.getAttribute("cate");
        log.info("pageRequestDTO:{}", pageRequestDTO);

        //서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isSearching", true);

        return "/community/data";
    }

    // 자료실 리스트
    @GetMapping("/data")
    public String data(Model model, PageRequestDTO pageRequestDTO) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "data");

        // 카테고리
        String cate = (String) session.getAttribute("cate");

        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO, cate);

        model.addAttribute(pageResponseDTO);
        model.addAttribute("isListing", true);  // isWriting=false로 설정하여 리스트 화면 표시

        return "/community/data";
    }

    // 자료실 글쓰기 페이지
    @GetMapping("/data/write")
    public String datawrite(Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("cate", "data");

        model.addAttribute("isWriting", true);  // isWriting=true로 설정하여 글쓰기 화면 표시
        return "/community/data";
    }

    // 자료실 글 등록
    @PostMapping("/data/write")
    public String datawrite(ArticleDTO articleDTO) {
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


        return "redirect:/community/data";  // 글쓰기 후 리스트 페이지로 리디렉션
    }

    // 자료실 글 보기 view
    @GetMapping("/data/view")
    public String dataview(Model model, int no){

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
        return "/community/data";
    }

    // 자료실 게시글 삭제
    @GetMapping("/data/delete")
    public String datadelete(int no){

        fileService.deletebasicFile(no);
        commentService.deletebasicAllComment(no);
        articleService.deletebasicArticle(no);
        return "redirect:/community/data";
    }


    // 자료실 게시글 수정
    @GetMapping("/data/modify")
    public String datamodify(int no, Model model) {

        // 수정 데이터 조회 서비스
        ArticleDTO articleDTO = articleService.findById(no);
        //모델 참조
        model.addAttribute("isModifying", true);
        model.addAttribute(articleDTO);

        return "/community/data";
    }

    // 자료실 게시글 수정
    @PostMapping("/data/modify")
    public String datamodify(ArticleDTO articleDTO) {
        //서비스 호출

        articleService.modifybasicArticle(articleDTO);

        // 리다이렉트
        return "redirect:/community/data";
    }
}
