package kr.co.greendae.repository.community.article;


import kr.co.greendae.entity.community.article.BasicArticle;
import kr.co.greendae.repository.community.custom.BasicArticleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicArticleRepository extends JpaRepository<BasicArticle, Integer>, BasicArticleRepositoryCustom {
    List<BasicArticle> findByCate(String cate);

    List<BasicArticle> findTop5ByCateOrderByNoDesc(String cate);
}
