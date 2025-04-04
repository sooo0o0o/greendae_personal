package kr.co.greendae.repository.support.impl;

import com.querydsl.core.Tuple;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.entity.Lecture.Register;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface RegisterRepositoryCustom {

    public Page<Tuple> findRegisterByStdNo(Pageable pageable, String stdNo);
    public Page<Tuple> findGradeByStdNo(Pageable pageable, String stdNo);

    Page<Tuple> selectAll(Pageable pageable);

    Page<Tuple> selectRegisterForSearch(PageRequestDTO pageRequestDTO, Pageable pageable);
}
