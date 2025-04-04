package kr.co.greendae.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private String cate = "free";

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 10;

    private String searchType;
    private String keyword;

    // 글목록 페이징 처리를 위한 Pageable 객체 생성 메서드
    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());
    }

}
