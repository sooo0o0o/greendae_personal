package kr.co.greendae.dto.page;

import kr.co.greendae.dto.community.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO {

    private List<ArticleDTO> dtoList;

    private String cate;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private boolean first, last; // 첫 페이지와 마지막 페이지 여부
    private int lastPage; // 마지막 페이지 번호 추가

    private String searchType;
    private String keyword;

    @Builder
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<ArticleDTO> dtoList, int total){
        this.cate = pageRequestDTO.getCate();
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.searchType = pageRequestDTO.getSearchType();
        this.keyword = pageRequestDTO.getKeyword();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        this.lastPage = (int) Math.ceil(total / (double) size); // 마지막 페이지 번호 계산
        this.end = Math.min(this.end, lastPage);
        this.prev = this.start > 1;
        this.next = this.pg < this.lastPage;

        // 추가: 첫 페이지와 마지막 페이지 여부
        this.first = (pg > 1);
        this.last = (pg < lastPage);
    }

}
