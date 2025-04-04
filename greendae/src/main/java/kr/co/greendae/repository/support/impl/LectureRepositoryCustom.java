package kr.co.greendae.repository.support.impl;

import com.querydsl.core.Tuple;
import kr.co.greendae.dto.support.pageRegister.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureRepositoryCustom {

    public Page<Tuple> selectAllByStdNoAndStdYear(Pageable pageable, int stdYear);
    public Page<Tuple> searchLecturesByStdNoAndStdYear(PageRequestDTO pageRequestDTO, Pageable pageable, int stdYear);
    Page<Tuple> selectAll(Pageable pageable);
    Page<Tuple> selectProfessorForSearch(kr.co.greendae.dto.page.PageRequestDTO pageRequestDTO, Pageable pageable);
}
