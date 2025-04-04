package kr.co.greendae.repository.department.custom;

import com.querydsl.core.Tuple;
import kr.co.greendae.dto.department.PageDepartmentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentRepositoryCustom {
    public Page<Tuple> selectAllForList(Pageable pageable);

    Page<Tuple> selectDepartmentForSearch(PageDepartmentRequestDTO pageRequestDTO, Pageable pageable);

}
