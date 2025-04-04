package kr.co.greendae.service;

import com.querydsl.core.Tuple;
import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.dto.page.PageResponseDTO;
import kr.co.greendae.entity.community.article.BasicArticle;
import kr.co.greendae.entity.community.article.ResStateArticle;
import kr.co.greendae.entity.community.article.StateArticle;
import kr.co.greendae.entity.user.User;
import kr.co.greendae.repository.community.article.BasicArticleRepository;
import kr.co.greendae.repository.community.article.ResStateArticleRepository;
import kr.co.greendae.repository.community.article.StateArticleRepository;
import kr.co.greendae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class QnaService {

    private final StateArticleRepository  stateArticleRepository;
    private final ResStateArticleRepository resStateArticleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void register(ArticleDTO articleDTO) {

        User user = userRepository.findById(articleDTO.getWriter()).get();

        StateArticle stateArticle = modelMapper.map(articleDTO, StateArticle.class);
        stateArticle.setUser(user);
        stateArticleRepository.save(stateArticle);

    }

    public PageResponseDTO findAll(PageRequestDTO pageRequestDTO, String cate) {


        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageArticle = stateArticleRepository.selectAllForList(pageable, cate);
        log.info("pageArticle : {}", pageArticle);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<ArticleDTO> articleDTOList = pageArticle.getContent().stream().map(tuple -> {

            StateArticle article = tuple.get(0, StateArticle.class);
            String nick = tuple.get(1, String.class);

            ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
            articleDTO.setNick(nick);

            return articleDTO;

        }).toList();

        int total = (int) pageArticle.getTotalElements();

        return PageResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(articleDTOList)
                .total(total)
                .build();
    }

    public ArticleDTO findById(int no) {
        return modelMapper.map(stateArticleRepository.findById(no), ArticleDTO.class);
    }

    @Transactional
    public StateArticle registerRes(ArticleDTO articleDTO) {

        int no = articleDTO.getNo();
        StateArticle stateArticle = stateArticleRepository.findById(no).get();
        stateArticle.setState("답변완료");

        stateArticleRepository.save(stateArticle);

        return stateArticle;

    }

    @Transactional
    public void saveRes(ResStateArticle stateArticle) {
        resStateArticleRepository.save(stateArticle);
    }

    public void saveRes(StateArticle stateArticle, ArticleDTO articleDTO) {
        ResStateArticle resStateArticle = modelMapper.map(articleDTO, ResStateArticle.class);
        resStateArticle.setStateArticle(stateArticle);
        resStateArticleRepository.save(resStateArticle);
    }

    @Transactional
    public void delete(int no) {
        stateArticleRepository.deleteById(no);
    }

    public void modifyQna(ArticleDTO articleDTO) {

        StateArticle stateArticle = stateArticleRepository.findById(articleDTO.getNo()).get();
        stateArticle.setContent(articleDTO.getContent());
        stateArticle.setTitle(articleDTO.getTitle());
        stateArticleRepository.save(stateArticle);
    }
}
