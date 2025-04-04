package kr.co.greendae.repository.community.comment;

import kr.co.greendae.entity.community.comment.BasicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicCommentRepository extends JpaRepository<BasicComment, Integer> {

    public List<BasicComment> findByParent(int parent);


    void deleteByParent(int no);

    void deleteById(int cno);
}
