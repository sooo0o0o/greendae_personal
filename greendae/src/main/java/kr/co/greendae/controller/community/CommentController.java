package kr.co.greendae.controller.community;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.dto.community.CommentDTO;
import kr.co.greendae.entity.community.article.BasicArticle;
import kr.co.greendae.entity.community.comment.BasicComment;
import kr.co.greendae.repository.community.comment.BasicCommentRepository;
import kr.co.greendae.service.ArticleService;
import kr.co.greendae.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;
    private final BasicCommentRepository basicCommentRepository;

    @ResponseBody
    @GetMapping("/comment/list")
    public List<CommentDTO> list(int parent){
        log.info("parent:{}", parent);

        List<CommentDTO> commentDTOList = commentService.findByParent(parent);

        return commentDTOList;
    }

    @ResponseBody
    @PostMapping("/comment/write")
    public CommentDTO write(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        log.info("commentDTO:{}", commentDTO);

        String regip = request.getRemoteAddr();
        commentDTO.setRegip(regip);

        CommentDTO savedCommentDTO = commentService.save(commentDTO);
        int no = savedCommentDTO.getParent();

        articleService.CountUpComment(no);


        // basisArticle - no pk findByID
        // .setComment(basicArticle.getCom + 1)


        return savedCommentDTO;
    }

    @ResponseBody
    @PostMapping("/comment/delete")
    public String delete(@RequestParam("cno") int cno, Principal principal) {
        BasicComment comment = basicCommentRepository.findById(cno).orElse(null);

        if (comment == null) {
            return "fail"; // 댓글이 존재하지 않으면 실패
        }

        String loginUserId = principal.getName(); // 현재 로그인한 사용자 ID
        String commentWriterId = comment.getUser().getUid(); // 댓글 작성자 ID

        if (!loginUserId.equals(commentWriterId)) {
            return "unauthorized"; // 작성자가 아니라면 삭제 불가
        }

        commentService.deletebasicComment(cno);
        return "success";
    }


    @PostMapping("/comment/update")
    public ResponseEntity<String> updateComment(@RequestBody CommentDTO commentDTO) {
        boolean result = commentService.updateComment(commentDTO);
        return result ? ResponseEntity.ok("success") : ResponseEntity.status(500).body("fail");
    }

}
