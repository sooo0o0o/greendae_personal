package kr.co.greendae.repository.community.file;

import kr.co.greendae.dto.community.FileDTO;
import kr.co.greendae.entity.community.article.BasicArticle;
import kr.co.greendae.entity.community.file.BasicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicFileRepository extends JpaRepository<BasicFile, Integer> {
    List<BasicFile> findByAno(int no);

    void deleteByAno(int ano);
}
