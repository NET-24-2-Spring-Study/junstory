package org.junstory.ex3.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Min(1)
    @Builder.Default
    private int page = 1;

    @Min(10)
    @Max(100)
    @Builder.Default
    private int pageSize = 10;

    public Pageable getPageable(Sort sort){
        return org.springframework.data.domain.PageRequest.of(page-1, pageSize, sort);
    }
}
