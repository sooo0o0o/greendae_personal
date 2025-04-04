package kr.co.greendae.repository.user.custom;

import com.querydsl.core.Tuple;
import kr.co.greendae.dto.page.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfessorRepositoryCustom {
    Page<Tuple> selectAllForList(Pageable pageable);

    Page<Tuple> selectProfessorForSearch(PageRequestDTO pageRequestDTO, Pageable pageable);
}
