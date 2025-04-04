package kr.co.greendae.repository.community.custom;

import com.querydsl.core.Tuple;
import kr.co.greendae.dto.page.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StateArticleRepositoryCustom {

    Page<Tuple> selectAllForList(Pageable pageable, String cate);
}
