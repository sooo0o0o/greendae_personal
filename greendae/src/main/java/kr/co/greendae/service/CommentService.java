package kr.co.greendae.service;


import kr.co.greendae.dto.community.CommentDTO;
import kr.co.greendae.entity.community.article.BasicArticle;
import kr.co.greendae.entity.community.comment.BasicComment;
import kr.co.greendae.entity.user.User;
import kr.co.greendae.repository.community.article.BasicArticleRepository;
import kr.co.greendae.repository.community.comment.BasicCommentRepository;
import kr.co.greendae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final BasicCommentRepository basicCommentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BasicArticleRepository basicArticleRepository;

    public List<CommentDTO> findByParent(int parent){

        List<BasicComment> basicCommentList = basicCommentRepository.findByParent(parent);

        List<CommentDTO> commentDTOList = basicCommentList.stream().map(entity -> {
            CommentDTO commentDTO = modelMapper.map(entity, CommentDTO.class);
            return commentDTO;
        }).toList();

        return commentDTOList;
    }

    public CommentDTO save(CommentDTO commentDTO){

        User user = userRepository.findById(commentDTO.getWriter()).get();

        BasicComment basicComment = modelMapper.map(commentDTO, BasicComment.class);
        basicComment.setUser(user);

        BasicComment savedComment = basicCommentRepository.save(basicComment);
        log.info("savedComment : {}", savedComment);

        return modelMapper.map(savedComment, CommentDTO.class);
    }

    @Transactional
    public void deletebasicAllComment(int no){
        basicCommentRepository.deleteByParent(no);
    }

    @Transactional
    public void deletebasicComment(int cno) {
        BasicComment comment = basicCommentRepository.findById(cno).get();
        BasicArticle basicArticle = basicArticleRepository.findById(comment.getParent()).get();
        basicArticle.setComment(basicArticle.getComment() - 1);
        basicArticleRepository.save(basicArticle);
        basicCommentRepository.deleteById(cno);
    }


    public boolean updateComment(CommentDTO commentDTO) {
        Optional<BasicComment> optionalComment = basicCommentRepository.findById(commentDTO.getCno());

        if (optionalComment.isPresent()) {
            BasicComment comment = optionalComment.get();
            comment.setContent(commentDTO.getContent());
            basicCommentRepository.save(comment); // 소문자로 변경
            return true;
        }

        return false;
    }
}
