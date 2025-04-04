package kr.co.greendae.service;



import com.querydsl.core.Tuple;
import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.dto.page.PageResponseDTO;
import kr.co.greendae.entity.community.article.BasicArticle;
import kr.co.greendae.entity.user.User;
import kr.co.greendae.repository.community.article.BasicArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final BasicArticleRepository basicArticleRepository;
    private final ModelMapper modelMapper;

    public int basicRegister(ArticleDTO articleDTO) {

        // 엔티티 변환
        User user = User.builder()
                .uid(articleDTO.getWriter())
                .build();

        BasicArticle basicArticle = modelMapper.map(articleDTO, BasicArticle.class);
        basicArticle.setUser(user);

        log.info("basicArticle : {}", basicArticle);

        // JPA 저장
        BasicArticle savedArticle = basicArticleRepository.save(basicArticle);

        basicArticleRepository.save(basicArticle);

        // 저장한 글번호 반환
        return savedArticle.getNo();
    }

    public List<ArticleDTO> findAllByCate(String freeboard) {

        List<BasicArticle> list =  basicArticleRepository.findByCate(freeboard);

        List<ArticleDTO> articleDTOList = new ArrayList<>();
        for (BasicArticle basicArticle : list) {
            ArticleDTO articleDTO = modelMapper.map(basicArticle, ArticleDTO.class);
            articleDTOList.add(articleDTO);
        }

        return articleDTOList;

    }

    public ArticleDTO findById(int no) {

        Optional<BasicArticle> optArticle = basicArticleRepository.findById(no);

        if(optArticle.isPresent()){
            BasicArticle basicArticle = optArticle.get();
            ArticleDTO articleDTO = modelMapper.map(basicArticle, ArticleDTO.class);

            basicArticle.setHit(articleDTO.getHit() + 1);
            basicArticleRepository.save(basicArticle);

            return articleDTO;
        }
        return null;
    }


    @Transactional
    public void deletebasicArticle(int no) {
        basicArticleRepository.deleteById(no);
    }


    @Transactional
    public void modifybasicArticle(ArticleDTO articleDTO) {

        // pk
        Optional<BasicArticle> optArticle = basicArticleRepository.findById(articleDTO.getNo());
        if (optArticle.isPresent()) {
            BasicArticle basicArticle = optArticle.get(); // 기존 데이터 조회

            // 새로운 값으로 업데이트
            basicArticle.setTitle(articleDTO.getTitle());
            basicArticle.setContent(articleDTO.getContent());

            if(articleDTO.getCate()!= null){
                basicArticle.setCate(articleDTO.getCate());  // 변경된 카테고리 반영
            }

            // 저장 (변경 감지가 자동으로 작동함)
            basicArticleRepository.save(basicArticle);
        } else {
            throw new IllegalArgumentException("해당 글이 존재하지 않습니다.");
        }

    }

    public void CountUpComment(int no) {

        Optional<BasicArticle> optArticle = basicArticleRepository.findById(no);

        if(optArticle.isPresent()){
            BasicArticle basicArticle = optArticle.get();
            basicArticle.setComment(basicArticle.getComment() + 1);
            basicArticleRepository.save(basicArticle);

        }

    }

    public PageResponseDTO searchAll(PageRequestDTO pageRequestDTO, String cate) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageArticle = basicArticleRepository.selectAllForSearch(pageRequestDTO, pageable, cate);
        log.info("pageArticle : {}", pageArticle);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<ArticleDTO> articleDTOList = pageArticle.getContent().stream().map(tuple -> {

            BasicArticle article = tuple.get(0, BasicArticle.class);
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

    public PageResponseDTO findAll(PageRequestDTO pageRequestDTO, String cate) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageArticle = basicArticleRepository.selectAllForList(pageable, cate);
        log.info("pageArticle : {}", pageArticle);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<ArticleDTO> articleDTOList = pageArticle.getContent().stream().map(tuple -> {

            BasicArticle basicArticle = tuple.get(0, BasicArticle.class);
            String nick = tuple.get(1, String.class);

            ArticleDTO articleDTO = modelMapper.map(basicArticle, ArticleDTO.class);
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


    public List<ArticleDTO> findAllByCateLimit5(String cate) {

        List<BasicArticle> basicArticle = basicArticleRepository.findTop5ByCateOrderByNoDesc(cate);
        List<ArticleDTO> list =  new ArrayList<>();
        for (BasicArticle basicArticle1 : basicArticle) {
            ArticleDTO articleDTO = modelMapper.map(basicArticle1, ArticleDTO.class);
            list.add(articleDTO);
        }

        return list;
    }
}
